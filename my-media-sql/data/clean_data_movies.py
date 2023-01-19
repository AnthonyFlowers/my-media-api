import os
import pandas as pd
import numpy as np
from sqlalchemy import create_engine
import mysql.connector
from config_loader import load_config

clean_data_csv = "movies_metadata_clean.csv"
data_csv = "movies_metadata.csv"

config = load_config()
db_username = config["username"]
db_password = config["password"]
host = config["host"]
database = config["database"]


# https://realpython.com/python-data-cleaning-numpy-pandas/
def clean_data():
    df = pd.read_csv(data_csv, low_memory=False)
    drop_columns = [
        "belongs_to_collection", "budget", "genres", "homepage",
        "original_language", "original_title", "popularity", "poster_path",
        "production_companies", "production_countries", "revenue",
        "spoken_languages", "status", "tagline", "video", "vote_average",
        "vote_count"
    ]
    # drop columns not used in app
    df.drop(drop_columns, inplace=True, axis=1)
    # only need the year for my app
    truncated_dates = df["release_date"].str.extract(r"^(\d{4})", expand=False)
    df["release_date"] = pd.to_numeric(truncated_dates)
    df["release_date"] = df["release_date"].fillna(0).apply(int)
    df["runtime"] = df["runtime"].fillna(0).apply(int)
    # save cleaned data to csv
    df.to_csv(clean_data_csv, escapechar="\\", quotechar="'", encoding="utf8")
    return df


def save_dataframe_to_database(dataframe):
    conn = create_engine(f"mysql+pymysql://{db_username}:{db_password}@{host}/{database}")
    dataframe.to_sql(
        "movies_metadata_temp_table",
        con=conn,
        if_exists="fail",
        chunksize=1000
        )


def tranasfer_data_to_movies_table():
    mysql_conn = mysql.connector.connect(
        host=host,
        user=db_username,
        password=db_password,
        database=database
    )

    cursor = mysql_conn.cursor()

    sql = """
    insert into movie (movie_name, movie_year, movie_length, movie_overview)
    select title, release_date, runtime, overview
    from movies_metadata_temp_table
    where title is not null;
    """
    cursor.execute(sql)
    mysql_conn.commit()

    cursor.execute("drop table movies_metadata_temp_table;")
    mysql_conn.commit()

    cursor.close()
    mysql_conn.close()


def main():
    data_frame = clean_data()
    save_dataframe_to_database(data_frame)
    tranasfer_data_to_movies_table()


if __name__ == "__main__":
    main()
