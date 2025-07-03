release:
	./gradlew ktlintCheck
	./gradlew generateChangelog
	./gradlew reckonTagPush
