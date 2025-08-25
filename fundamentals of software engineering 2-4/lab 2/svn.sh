#!/bin/sh

cd svn

# Очистка рабочей директории
rm -rf working-copy
rm -rf repo

# Инициализация нового репозитория SVN
echo "Создаю новый репозиторий SVN..."
svnadmin create repo
REPO_URL="file://$(pwd)/repo"
echo "Репозиторий создан!"

# Создание trunk и branches
echo "Создаю trunk и branches..."
svn mkdir -m "Создание trunk и branches" "$REPO_URL/trunk" "$REPO_URL/branches" 
echo "Trunk и branches созданы!"

# Checkout в рабочую копию
echo "Checkout в рабочую копию..."
svn checkout "$REPO_URL/trunk" working-copy
cd working-copy
echo "Рабочая копия получена!"

# Первый коммит
echo "Распаковываю первый коммит..."
unzip ../commits/commit0.zip
svn add *
svn commit -m "Initial commit (r0)" --username=red
echo "Коммит 0 (r0) создан!"

# Создание и переключение на ветку branch2
echo "Создаю и переключаюсь на ветку branch2..."
svn copy "$REPO_URL/trunk" "$REPO_URL/branches/branch2" -m "Создание ветки branch2"
svn switch "$REPO_URL/branches/branch2"
echo "Ветка branch2 создана и переключились на нее!"

# Второй коммит
echo "Распаковываю второй коммит..."
unzip -o ../commits/commit1.zip
svn status
echo "Файлов для добавления нет!"
svn commit -m "Revision 1 (r1)" --username=blue
echo "Коммит 1 (r1) создан!"

# Создание и переключение на ветку branch3
echo "Создаю и переключаюсь на ветку branch3..."
svn copy "$REPO_URL/trunk" "$REPO_URL/branches/branch3" -m "Создание ветки branch3"
svn switch "$REPO_URL/branches/branch3"
echo "Ветка branch3 создана и переключились на нее!"

# Третий коммит
echo "Распаковываю третий коммит..."
unzip -o ../commits/commit2.zip
svn status
svn add BqUYnZcmHF.d4Y
svn commit -m "Revision 2 (r2)" --username=blue
echo "Коммит 2 (r2) создан!"

# Переключение обратно на trunk и последующие коммиты
echo "Переключение обратно на trunk..."
svn switch "$REPO_URL/trunk"
unzip -o ../commits/commit3.zip
svn status
echo "Файлов для добавления нет!"
svn commit -m "Revision 3 (r3)" --username=red
echo "Коммит 3 (r3) создан!"

unzip -o ../commits/commit4.zip
svn status
svn add Jw1Pd4YBqU.rdb
svn commit -m "Revision 4 (r4)" --username=red
echo "Коммит 4 (r4) создан!"

# Переключение на ветку branch2 и добавление файлов
echo "Переключение на ветку branch2..."
svn switch "$REPO_URL/branches/branch2"
unzip -o ../commits/commit5.zip
svn status
svn add Jw1Pd4YBqU.rdb
svn commit -m "Revision 5 (r5)" --username=blue
echo "Коммит 5 (r5) создан!"

# Переключение на ветку branch3 и коммит
echo "Переключение на ветку branch3..."
svn switch "$REPO_URL/branches/branch3"
unzip -o ../commits/commit6.zip
svn status
echo "Файлов для добавления нет!"
svn commit -m "Revision 6 (r6)" --username=blue
echo "Коммит 6 (r6) создан!"

# Переключение на ветку trunk и коммит
echo "Переключение на ветку trunk..."
svn switch "$REPO_URL/trunk"
unzip -o ../commits/commit7.zip
svn status
echo "Файлов для добавления нет!"
svn commit -m "Revision 7 (r7)" --username=red
echo "Коммит 7 (r7) создан!"

# Переключение на branch2 и коммит
echo "Переключение на ветку branch2..."
svn switch "$REPO_URL/branches/branch2"
unzip -o ../commits/commit8.zip
svn status
echo "Файлов для добавления нет!"
svn commit -m "Revision 8 (r8)" --username=blue
echo "Коммит 8 (r8) создан!"

# Переключение на branch3 и коммит
echo "Переключение на ветку branch3..."
svn switch "$REPO_URL/branches/branch3"
unzip -o ../commits/commit9.zip
svn status
svn add Jw1Pd4YBqU.rdb
svn commit -m "Revision 9 (r9)" --username=blue
echo "Коммит 9 (r9) создан!"

# Продолжение с коммитами 10 и 11
unzip -o ../commits/commit10.zip
svn status
echo "Файлов для добавления нет!"
svn commit -m "Revision 10 (r10)" --username=blue
echo "Коммит 10 (r10) создан!"

unzip -o ../commits/commit11.zip
svn status
svn add "*"
svn commit -m "Revision 11 (r11)" --username=blue
echo "Коммит 11 (r11) создан!"

# Мердж ветки branch3 в trunk
echo "Мердж ветки branch3 в trunk..."
svn switch "$REPO_URL/trunk"
svn merge "$REPO_URL/branches/branch3"
echo "Возник конфликт!"

# Просмотр конфликтов в файлах E.java и F.java
echo "Просмотрим конфликт в файле E.java..."
cat E.java
echo "Просмотрим конфликт в файле F.java..."
cat F.java
echo "Просмотрим tree конфликт в файле Jw1Pd4YBqU.rdb..."
echo "Jw1Pd4YBqU.rdb в trunk:"
cat Jw1Pd4YBqU.rdb
echo \n
echo "Jw1Pd4YBqU.rdb в branch3:"
svn cat "$REPO_URL/branches/branch3/Jw1Pd4YBqU.rdb"
echo \n

# Разрешение конфликта
svn resolve --accept=theirs-full E.java F.java
svn resolve --accept=working Jw1Pd4YBqU.rdb
echo "Конфликт разрешен, выбрана версия из branch3 для E.java и F.java, Jw1Pd4YBqU.rdb оставлен из рабочей версии."

svn delete "$REPO_URL/branches/branch3" -m "Ветка branch3 удалена"
echo "Ветка branch3 удалена."


# Следующий коммит
unzip -o ../commits/commit12.zip
svn status
svn add 2kQLJvaRQh.DS4
svn commit -m "Revision 12 (r12)" --username=red
echo "Коммит 12 (r12) создан!"

# Переключение на branch2 и последний коммит
svn switch "$REPO_URL/branches/branch2"
unzip -o ../commits/commit13.zip
svn status
svn add "*"
svn commit -m "Revision 13 (r13)" --username=blue
echo "Коммит 13 (r13) создан!"

# Мердж ветки branch2 в trunk
svn switch "$REPO_URL/trunk"
svn merge "$REPO_URL/branches/branch2"
echo "Возник конфликт!"

# Просмотр конфликтов в файлах E.java и F.java
echo "Просмотрим конфликт в файле E.java..."
cat E.java
echo "Просмотрим конфликт в файле F.java..."
cat F.java

echo "Просмотрим tree конфликт в файле *..."
echo "* в trunk:"
cat "*"
echo \n"* в branch3:"
svn cat "$REPO_URL/branches/branch2/*"
echo \n

echo "Просмотрим tree конфликт в файле Jw1Pd4YBqU.rdb..."
echo "Jw1Pd4YBqU.rdb в trunk:"
cat Jw1Pd4YBqU.rdb
echo \n
echo "Jw1Pd4YBqU.rdb в branch2:"
svn cat "$REPO_URL/branches/branch2/Jw1Pd4YBqU.rdb"
echo \n

# Разрешение конфликта
svn resolve --accept=mine-full E.java F.java 
svn resolve --accept=working "*" Jw1Pd4YBqU.rdb
echo "Конфликт разрешен, выбрана версия из trunk для всех файлов."

svn delete "$REPO_URL/branches/branch2"
echo "Ветка branch2 удалена."

# Финальный коммит
unzip -o ../commits/commit14.zip
svn status
svn add CedXKASAnu.Bl5 D.java G.java
svn commit -m "Revision 14 (r14)" --username=red
echo "Коммит 14 (r14) создан!"

echo "Все шаги выполнены успешно!"

# svn update -r 16
# svn merge -r 17^:19 .

