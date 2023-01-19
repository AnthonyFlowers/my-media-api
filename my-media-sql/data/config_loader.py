import json

def load_config():
    with open("db-config.json") as file:
        config = json.load(file)
    assert config.get("host", "") != "", "db-config file must have a host attribute"
    assert config.get("username", "") != "", "db-config file must have a username attribute"
    assert config.get("password", "") != "", "db-config file must have a password attribute"
    assert config.get("database", "") != "", "db-config file must have a database attribute"
    return config