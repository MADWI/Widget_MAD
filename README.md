#Widget studenta Wydziału Informatyki ZUT
###dla systemu Android

![widget](http://www.mad.zut.edu.pl/images/widget_old.png)

##AUTORZY:
![logo](http://www.mad.zut.edu.pl/images/logo-mini.png)
###SKN [Mobile Applications Developers](http://www.mad.zut.edu.pl/news.php), WI ZUT.


##CHANGELOG

###Wersja 0.11

NOWE

- poprawiono obsługe braku łączności z Internetem
- kilka innych drobnych poprawek
- zmiana numeracji wesji

BRAK

- pobierania planu i grup ze strony WI.

BUGI

- nie działa pobiernie grupy ponieważ nie ma planu na stronie WI, klasa MyPreferences zostanie wtedy przebudowana 
do poprawnego działania.
- czasami  przy słabym Internecie odswieżenie powoduje wyjątek NullPointerException w klasie WeekParityChecker

###Wersja 0.10

NOWE

- przeniesiono długtrwałe operację do serwisu, który aktualizuje widget.
- dodano klasę Intents ( tworzenie potrzebnych intencji) , Constans ( zawierającą stałe ) oraz 
SharedPrefUtils ( pomagającą w pracy z SharedPreferences ).
- automatyczne odswieżanie co godzine lub ręczne poprzez przycisk.

BRAK

- pobierania planu i grup ze strony WI.

BUGI

- nie działa pobiernie grupy ponieważ nie ma planu na stronie WI, klasa MyPreferences zostanie wtedy przebudowana 
do poprawnego działania.
- czasami odswieżenie powoduje wyjątek NullPointerException w klasie WeekParityChecker

###Wersja 0.05

NOWE:

- grupy wyswietlaja sie po pierwszym wyborze

BUGI

- chcac zmienic grupe nie wychodzac z ustawien wyswietla sie ona po 2 lub 3 kliknieciu 

###Wersja 0.04

NOWE:

- dodanie przycisku "o autorach" w ustawieniach

BUGI

- nie dziala wylaczanie automatycznego odswieżania (jak w wersji 0.03)

###Wersja 0.03

NOWE:

- przezroczystość okna widgetu

BUGI

- nie dziala wylaczanie automatycznego odswieżania

###Wersja 0.02

NOWE:

- obsługa sytuacji w których brak połączenia z Internetem
- wyświetlanie ostatniej zmiany w planie
- Button do pobrania planu
- usunięcie błędów przy chowaniu Progress Bara
- pobieranie samej daty ( bez czasu ) 

BUGI

- nie dziala wylaczanie automatycznego odswieżania

###Wersja 0.01

NOWE:

- dodano aktywnosc ustawien
- dodano aktywnosc zmian w planie
- dodano ProgressBar
- dodano niewidoczny przycisk na ostatniej zmianie w planie
- ustawiono akcje onClick dla Buttonow
- poprawiono wyświetlanie daty systemowej


BUGI:

- czasami przy braku po³¹czenia z Internetem zmiany wyrzucają NullPointerException
- nie wyswietla siê ostatnia zmiana w planie
- zbyt powolne pobieranie daty na starcie 