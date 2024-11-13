# MalifauxSplitter

Java App used to split Malifaux 3rd Edition PDFs into front and back sets to be printed out.

To use this app:
1. Start up the application locally
2. Add any files you want split to the build/resources/main/unprocessed folder
3. Hit the split endpoint in your browser: localhost:8081/split/unprocessed by default
4. All unprocessed PDF files will be processed and split into front and backed and stored in the /build/resources/main/processed folder.
5. All names are persisted with a 1 and 2 added for front and back

To run the stitching endpoint after the processing has finished:
1. Hit the stitching endpoint in your bowser or Postman: http://localhost:8081/join-processed by default
2. The script will read all files in the /build/resources/main/processed folder
3. The script will match fronts and back, convert them to PNG and store in the /build/resources/main/converted folder
4. The script will then stitch the converted images into a joined image and store in the /build/resources/main/stitched folder
5. The stitched images should be of the correct size and format (1650 x 1450px)

Please note any artwork, names or trademarks belong to Wyrd Miniatures
