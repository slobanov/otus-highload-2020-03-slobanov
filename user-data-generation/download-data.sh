# last names
kaggle datasets download -d jojo1000/facebook-last-names-with-count
unzip -d data facebook-last-names-with-count.zip

# first names
kaggle datasets download -d nltkdata/names
unzip -d data names.zip

# cities
kaggle datasets download -d okfn/world-cities
unzip -d data world-cities

# interests
kaggle datasets download -d muhadel/hobbies
unzip -d data hobbies

# cleanup
rm *.zip
