release:
	./gradlew ktlintCheck
    ./gradlew currentVersion
	./gradlew generateChangelog
	./gradlew release
