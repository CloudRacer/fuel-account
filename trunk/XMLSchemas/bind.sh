#!/bin/sh
 
processRequestOfBindScript() {
	INT2XSD_PROGRAM=~/apps/development/xmlbeans-2.6.0/bin
	XJC_PROGRAM=xjc

	# Create an XSD Schema based on an XML sample.
	XML_SAMPLE_NAME=vehicles
	XSD_NAME=`echo $XML_SAMPLE_NAME`0
	"$INT2XSD_PROGRAM/inst2xsd" "$SCRIPT_FOLDER/vehicles.xml" -outPrefix "$XML_SAMPLE_NAME" "$SCRIPT_FOLDER/$XML_SAMPLE_NAME.xml"
	mv -f "$SCRIPT_FOLDER/$XSD_NAME.xsd" "$SCRIPT_FOLDER/$XML_SAMPLE_NAME.xsd"

	# Create the Java Classes.
	CLASSES_FOLDER="$SCRIPT_FOLDER/../FuelAccount/src/"
	PACKAGE_NAME="uk.org.mcdonnell.fuelaccount.schemas"
	echo "CLASSES_FOLDER:$CLASSES_FOLDER."
	if [ ! -d "$CLASSES_FOLDER" ]; then
		mkdir "$CLASSES_FOLDER"
	fi
	"$XJC_PROGRAM" "$SCRIPT_FOLDER/$XML_SAMPLE_NAME.xsd" -d "$CLASSES_FOLDER" -p "$PACKAGE_NAME"
}

initialiseEnvironmentOfBindScript() {
	getApplicationFoldernameOfBindScript
	if [ $? -ne 0 ]; then
		return 1
	fi

	return 0
}

getApplicationFoldernameOfBindScript() {
	SCRIPT_FILE=`basename $0`
	RESULT=$?
	if [ $RESULT -ne 0 ]; then
		echo "ERR: $RESULT: Error encountered while determining the name of the current script."
		return $RESULT;
	fi
	
	SCRIPT_FOLDER=`dirname $0`;
	RESULT=$?
	if [ $RESULT -ne 0 ]; then
		echo "ERR: $RESULT: Error encountered while determining the name of the folder containing the current script."
		return $RESULT;
	fi

	if [ "$SCRIPT_FOLDER" = "" ] || [ "$SCRIPT_FOLDER" = "." ] || [ -z "$SCRIPT_FOLDER" ]; then
		SCRIPT_FOLDER=`pwd`
	fi

	return 0
}

mainOfBindScript() {
	initialiseEnvironmentOfBindScript 
	if [ $? -ne 0 ]; then
		return 1
	fi

	processRequestOfBindScript
	if [ $? -ne 0 ]; then
		return 1
	fi
	
	return 0
}

mainOfBindScript

exit $?