Реализовать консольное приложения список задач.

Как хранилище используется база данных MySQL.
Приложение должно быть организовать таким образом чтобы можно было на основе него написать другое приложение например веб.
При запуске выбирается одно из действий:
1) Добавить задачу;
2) Вывести список задач;
3) Выйти.

При выборе "Добавить задачу" задачу нужно вводить:	
1) Название задачи;
2) Когда должна быть выполнена;
3) Приоритет.
После окончания ввода возвращаемся в основное меню.

При выборе "Вывести список задач" 
1) Выводиться список задач, если задача просрочена она помечается как просроченная. После указания какая задача выполнена возвращаемся на екран "список задач"
2) На екране можно указать какая задача закончена, когда задача заканчивается она переноситься в другую таблицу и видна при выборе всех завершенных.

3) На екране можно  запросить вывод всех завершённых задач
4) На екране можно  вернуться в основе меню/екран.

Использовать Java Core, без ORM, без фреймворков. 