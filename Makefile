app:
	rm -rf ./build
	gradle build
	cp -f ./build/libs/FitManager-1.0.0.jar ./docker/backend/fitmanager.jar
