# AXMLEditor
A simple editor for changing your encoded AndroidManifest.xml.

Reference: https://code.google.com/archive/p/android4me/

## Feature
1. Read encoded AndroidManifest.xml file to chunk array.
2. Change contents in chunk array directly.
3. Rebuild encoded AndroidManifest.xml.

## TODO
The project only can change text data which is a direct word in the AndroidManifest.xml.
It does not work even through the text is a String data in Android Resources.
But it's an easy work to develop Classes extends the Base.
