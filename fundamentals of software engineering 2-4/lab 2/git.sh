#!/bin/sh

cd git

# Очистка рабочей директории
rm -f *
rm -rf .git
rm -f .gitignore

# Инициализация нового Git репозитория
echo "Создаю новый локальный репозиторий Git..."
git init
echo "Локальный репозиторий создан!"

# Создание .gitignore
echo "Создаю файл .gitignore..."
echo ".DS_Store" > .gitignore
echo "commits" >> .gitignore
echo "git.sh" >> .gitignore
git add .gitignore
git commit -m "Создан .gitignore"
echo "Файл .gitignore создан и закоммичен!"

# Настройка пользователя Git
echo "Настрою пользователя Git..."
git config user.name "red"
git config user.email "red@example.com"
echo "Пользователь red создан!"

# Создание первой ветки и коммит
git checkout -b branch1
echo "Первая ветка branch1 создана!"

unzip commits/commit0.zip
git add .
git commit -m "Initial commit (r0)"
echo "Коммит 0 (red) создан!"

# Создание второй ветки и коммит
git checkout -b branch2
echo "Вторая ветка branch2 создана!"

unzip -o commits/commit1.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 1 (r1)"
echo "Коммит 1 (blue) создан!"

# Создание третьей ветки и коммит
git checkout -b branch3
echo "Третья ветка branch3 создана!"

unzip -o commits/commit2.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 2 (r2)"
echo "Коммит 2 (blue) создан!"

# Переход на branch1 и коммиты
git checkout branch1
echo "Переключение на ветку branch1..."

unzip -o commits/commit3.zip
git add .
git commit -m "Revision 3 (r3)"
echo "Коммит 3 (red) создан!"

unzip -o commits/commit4.zip
git add .
git commit -m "Revision 4 (r4)"
echo "Коммит 4 (red) создан!"

# Переход на branch2 и коммиты
git checkout branch2
echo "Переключение на ветку branch2..."

unzip -o commits/commit5.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 5 (r5)"
echo "Коммит 5 (blue) создан!"

# Переход на branch3 и коммиты
git checkout branch3
echo "Переключение на ветку branch3..."

unzip -o commits/commit6.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 6 (r6)"
echo "Коммит 6 (blue) создан!"

# Переход на branch1 и коммиты
git checkout branch1
echo "Переключение на ветку branch1..."

unzip -o commits/commit7.zip
git add .
git commit -m "Revision 7 (r7)"
echo "Коммит 7 (red) создан!"

# Переход на branch2 и коммиты
git checkout branch2
echo "Переключение на ветку branch2..."

unzip -o commits/commit8.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 8 (r8)"
echo "Коммит 8 (blue) создан!"

# Переход на branch3 и коммиты
git checkout branch3
echo "Переключение на ветку branch3..."

unzip -o commits/commit9.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 9 (r9)"
echo "Коммит 9 (blue) создан!"

unzip -o commits/commit10.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 10 (r10)"
echo "Коммит 10 (blue) создан!"

unzip -o commits/commit11.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 11 (r11)"
echo "Коммит 11 (blue) создан!"

# Мердж ветки branch3 в branch1
git checkout branch1
echo "Мердж ветки branch3 в branch1..."

git merge branch3
echo "Возник конфликт при мердже!"

# Просмотрим конфликты в файлах E.java и F.java
echo "Просмотрим конфликт в файле E.java..."
cat E.java
echo "Просмотрим конфликт в файле F.java..."
cat F.java

# Разрешаем конфликт, выбираем версию из branch3
git checkout --theirs .

echo "Конфликт разрешен, выбрана версия из branch3."

git add .
git branch -D branch3
echo "Ветка branch3 удалена."

# Создание коммита после разрешения конфликта
unzip -o commits/commit12.zip
git add .
git commit -m "Revision 12 (r12)"
echo "Коммит 12 (red) создан!"

# Переход на branch2 и коммиты
git checkout branch2
echo "Переключение на ветку branch2..."

unzip -o commits/commit13.zip
git add .
git commit --author="blue <blue@example.com>" -m "Revision 13 (r13)"
echo "Коммит 13 (blue) создан!"

# Мердж ветки branch2 в branch1
git checkout branch1
echo "Мердж ветки branch2 в branch1..."

git merge branch2
echo "Возник конфликт при мердже!"

# Просмотрим конфликты в файлах E.java и F.java
echo "Просмотрим конфликт в файле E.java..."
cat E.java
echo "Просмотрим конфликт в файле F.java..."
cat F.java

# Разрешаем конфликт, выбираем версию из branch1 (ours)
git checkout --ours .
echo "Конфликт разрешен, выбрана версия из branch1."

git add .
git branch -D branch2
echo "Ветка branch2 удалена."

# Финальный коммит
unzip -o commits/commit14.zip
git add .
git commit -m "Revision 14 (r14)"
echo "Коммит 14 (red) создан!"

echo "Все шаги выполнены успешно!"



# git reset --hard 3379ee6f7e4cc1f1e5bdd299d1ff82a3dc801ae0
# git reset --soft 3379ee6f7e4cc1f1e5bdd299d1ff82a3dc801ae0
# git reset --mixed 3379ee6f7e4cc1f1e5bdd299d1ff82a3dc801ae0

# git revert 3cef7a6d2e3aab32cb4bc46cace1f1ff920edb59^..847ad8a7e90f641724e3c3375160cecbce622c0f




