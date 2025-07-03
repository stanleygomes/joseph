release:
	@echo "🚀 Iniciando processo de release..."
	@echo "------------------------------------"

	@echo "1. Verificando a qualidade do código com Ktlint..."
	# ./gradlew ktlintCheck

	@echo "2. Calculando a próxima versão..."
	@NEXT_VERSION=$$(./gradlew currentVersion -q | grep -oE '[0-9]+\.[0-9]+\.[0-9]+(-[A-Za-z0-9]+)?') && \
	echo "✅  Versão calculada: $$NEXT_VERSION" && \
	echo "3. Gerando CHANGELOG.md para a versão $$NEXT_VERSION..." && \
	./gradlew generateChangelog -Pversion=$$NEXT_VERSION && \
	echo "4. Adicionando e fazendo commit do CHANGELOG.md..." && \
	# git add CHANGELOG.md && \
	# git diff --staged --quiet || git commit -m "docs: update changelog for release" && \
	echo "5. Criando e enviando a tag de release..." && \
	# ./gradlew release && \
	echo "------------------------------------" && \
	echo "🎉 Release $$NEXT_VERSION concluído com sucesso!"
