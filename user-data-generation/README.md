# user-data-generation

Scripts to generate dummy data for social network.

* [download-data.sh](download-data.sh) - downloads data sets from [kaggle](https://www.kaggle.com/). All data will be unpacked into `data` folder.
You need to install and authorise into [kaggle CLI](https://github.com/Kaggle/kaggle-api) in order to run this script. List of data sets:
  * [facebook-last-names-with-count](https://www.kaggle.com/jojo1000/facebook-last-names-with-count) - last names
  * [nltkdata/names](https://www.kaggle.com/nltkdata/names) - male and female first names
  * [world-cities](https://www.kaggle.com/okfn/world-cities) - cities
  * [hobbies](https://www.kaggle.com/muhadel/hobbies) - interests
* [data-generation.ipynb](data-generation.ipynb) - jupyter notebook with data generation code
  * By default 1_000_000 users are generated
  * Every user has password `password123`, role `USER` and random string login
  * There are only two gendres - `Male` and `Female`, 
  all users are are randomly splitted by it
  * Dates of birth also generated randomly
  * Each user has some randomly assigned interest and followers
  * Data generation results in csv files, which are copied back to social-network
  [application resources](../social-network/src/main/resources/db/changelog/data).
  You just need to start the application with a clean database and let liquibase to do it's job
