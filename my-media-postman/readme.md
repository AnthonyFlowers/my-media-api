# Using postman through newman

### Required node packages
`npm install -g newman`

### Running newman
1. Export collection
2. Navigate to the postman collection location
3. run `newman run postman_collection_name.json`

### Running newman with the html reporter

1. Install the html reporter package `npm install -g newman-reporter-htmlextra`
2. Navigate to the postman collection location
3. run `newman run postman_collection_name.json --reporters cli,htmlextra`
    - this will run the postman collection and output the runs results to the cli and in an html document

### Running using api access instead of exports
1. In postman select the share option for a collection
2. Select the Via API tab
3. Generate access key if needed
4. Copy and use the access url in place of the collection name
`newman run https://api.postman.com/collections/\[collection_identifier\]?access_key=\[access_key\]`

## My Tests
- Any tests in the my-media-postman directory will use collection variables for handling api location and user details