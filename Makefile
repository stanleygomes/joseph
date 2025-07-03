release:
	./gradlew ktlintCheck
	./gradlew generateChangelog
	git add CHANGELOG.md
	# Apenas faz o commit se o changelog foi realmente alterado
	git diff --staged --quiet || git commit -m "docs: update changelog for release"
	./gradlew release
