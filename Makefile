release:
	./gradlew ktlintCheck
	NEXT_VERSION=$$(./gradlew release -q -Prelease.dryRun=true | grep 'Project version set to' | awk '{print $$5}')
	./gradlew generateChangelog -Pversion=$(NEXT_VERSION)
	git add CHANGELOG.md
	# Apenas faz o commit se o changelog foi realmente alterado
	git diff --staged --quiet || git commit -m "docs: update changelog for release"
	./gradlew release -x generateChangelog
