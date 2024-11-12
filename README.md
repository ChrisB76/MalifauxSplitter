# MalifauxSplitter

Java App used to split Malifaux 3rd Edition PDFs into front and back sets to be printed out.

To use this app:
1. Start up the application locally
2. Add any files you want split to the build/resources/main/unprocessed folder
3. Hit the split endpoint in your browser: localhost:8081/split/unprocessed by default
4. All unprocessed PDF files will be processed and split into front and backed and stored in the /build/resources/main/processed folder.
5. All names are persisted with a 1 and 2 added for front and back

Please note any artwork, names or trademarks belong to Wyrd Miniatures
