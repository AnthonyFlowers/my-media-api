import os
import pandas as pd
import numpy as np
from sqlalchemy import create_engine
import mysql.connector
from config_loader import load_config

clean_data_csv = "tv_show_titles_clean.csv"
data_csv = "tv_show_titles.csv"

config = load_config()
db_username = config["username"]
db_password = config["password"]
host = config["host"]
database = config["database"]


# https://realpython.com/python-data-cleaning-numpy-pandas/
def clean_data():
    df = pd.read_csv(data_csv, low_memory=False)

    # drop columns not used in app
    # keeping title, type, description, release_year and seasons
    drop_columns = [
        "id", "age_certification", "runtime", "genres", "production_countries",
        "imdb_id", "imdb_score", "imdb_votes", "tmdb_popularity", "tmdb_score"
    ]
    df.drop(drop_columns, inplace=True, axis=1)
    # only need the year for my app
    # truncated_dates = df["release_date"].str.extract(r"^(\d{4})", expand=False)
    # df["release_date"] = pd.to_numeric(truncated_dates)
    df["seasons"] = df["seasons"].fillna(0).apply(int)
    # df["runtime"] = df["runtime"].fillna(0).apply(int)
    shows = df.loc[df['type'] == 'SHOW']
    # save cleaned data to csv
    shows.to_csv(clean_data_csv,
                 escapechar="\\",
                 quotechar="'",
                 encoding="utf8")
    return df


def save_dataframe_to_database(dataframe):
    conn = create_engine(
        f"mysql+pymysql://{db_username}:{db_password}@{host}/{database}")
    dataframe.to_sql("tv_shows_temp_table",
                     con=conn,
                     if_exists="fail",
                     chunksize=1000)


def tranasfer_data_to_tables():
    mysql_conn = mysql.connector.connect(host=host,
                                         user=db_username,
                                         password=db_password,
                                         database=database)

    cursor = mysql_conn.cursor()

    sql = """
    insert into tv_show (tv_show_name, tv_show_overview, tv_show_release_year)
    select title, description, release_year
    from tv_shows_temp_table
    where title is not null;
    """
    cursor.execute(sql)
    mysql_conn.commit()
    cursor.execute("drop table tv_shows_temp_table;")
    mysql_conn.commit()

    cursor.close()
    mysql_conn.close()


def main():
    data_frame = clean_data()
    save_dataframe_to_database(data_frame)
    tranasfer_data_to_tables()


if __name__ == "__main__":
    main()
