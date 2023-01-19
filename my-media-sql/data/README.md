### Using some data from The Movies Dataset from Kaggle

[The Movies Dataset](https://www.kaggle.com/datasets/rounakbanik/the-movies-dataset)
Download the `movies_metadata.csv` into this folder

[Netflix TV Shows and Movies](https://www.kaggle.com/datasets/victorsoeiro/netflix-tv-shows-and-movies)
Download the `titles.csv` into this folder and rename it to `tv_show_titles.csv`

### Install python
Install python and the numpy and pandas libraries

## Create db-config.json
Create a config file `db-config.json` that stores the database connection info
The json file should look like this:
```
{
    "host": "localhost",
    "username": "username",
    "password": "password",
    "database": "database_name"
}
```

### Cleaning and adding the data to the database
Clean and insert the data using python, pandas and numpy by running the following commands:
`py clean_data_movies.py`
`py clean_data_tv_shows.py`