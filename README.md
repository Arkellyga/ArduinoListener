# ArduinoListener
Приложение является конструктором, который работает с usb портом мобильного устройства и обрабатывает информацию, визуализируя в пользовательском виде.
# О приложении
Приложение представляет собой USB слушателя, который находится в ожидании подключения какого-либо устройства, на базе чипа CH340 и его аналогов.
Ключевой функцией является получение данных с USB шины и последующая визуализация в удобном пользователю виде.
Пользователь приложения оперирует, так называемыми, плиткам, каждую из которых он может настроить на прием какого-то конкретного сообщения из устройства.
Каждую плитку можно настроить, придать ей свой цвет, рамку, действие на нажатие, настройка шрифта.
Для настроек пользователю предоставляется удобный редактор. Также на плитки можно выводить системную информацию с мобильного устройства.
Информация на плитках обрабатывается с общего потока usb, при помощи RegExp. Поэтому для работы с приложением необходимы минимальные знания данного языка
# Текущее состояние
- На текущий момент приложение находится в заморозке, так как имеются ограничения во времени, а также более интересные проекты. По возможности буду продолжать работу.
# TODO
- Планируется добавить английский язык
- Пофиксить логические баги
- Полный рефакторинг кода
- Будет написана маленькая вики, для понимания принципа работы плиток
# Библиотеки
В приложении используются следующие библиотеки:
- <a href="https://github.com/mik3y/usb-serial-for-android" target="_blank">usb-serial-for-android </a>
- <a href="https://github.com/yukuku/ambilwarna/tree/master/library">ambilwarna</a>
