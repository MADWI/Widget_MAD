#Widget studenta Wydziału Informatyki ZUT
###dla systemu Android

![widget](http://www.mad.zut.edu.pl/images/widget_old.png)

##AUTORZY:
![logo](http://www.mad.zut.edu.pl/images/logo-mini.png)
###SKN [Mobile Applications Developers](http://www.mad.zut.edu.pl/news.php), WI ZUT.


##CHANGELOG

###Wersja 0.60

NOWE

- możliwość otwarcia planu z poziomu listy planów
- nowa ikona aplikacji

POPRAWIONO

- poprawiony błąd tworzenia pliku "brak.pdf"
- w tej wersji brak zalegania aktywności na stosie


###Wersja 0.50

NOWE

- po kliknięciu na rodzaj dnia zostanie wyświetlona pełna lista dni tygodnia

POPRAWIONE

- poprawiono prędkość działania i reakcji widgetu
- zmniejszono transfer danych

###Wersja 0.40

NOWE

- zmiana wygladu layoutu
- zmieniony sposob parsowania dni tygodnia
- dodano sprawdzanie parzystosci dla dnia nastepnego
- dodano dokumentacje

###Wersja 0.40

NOWE

- zmiana wygladu layoutu
- zmieniony sposob parsowania dni tygodnia
- dodano sprawdzanie parzystosci dla dnia nastepnego
- dodano dokumentacje

###Wersja 0.30

NOWE

- parzystosc dla dnia biezacego i nastepnego
- nowe ustawienia
- mozliwosc usuwania planow
- poprawione wyswietlanie ostaniej zmiany w planie


###Wersja 0.20

NOWE

- pobieranie planu oraz wyświetlanie go po kliknięciu na nazwę grupy

BUGI

- czasami występuję wyjątek NullPointerException w klasie WeekParityChecker

###Wersja 0.12

NOWE

- dodano nową aktywność ustawien 
- działa pobieranie grup i zapisywanie do SharedPreferences
- przy dodawaniu widgetu po raz pierwszy, wyświetla się aktywność ustawień grupy
- odswieżony Dialog About 
- po nacisnięciu logo WI w przeglądarce otwiera się strona MAD
- dodano obsluge sytuacji w której brakuje zmian w planie na stronie WI

BRAK

- pobierania planu dla konkretnej grupy

BUGI

- czasami występuję wyjątek NullPointerException w klasie WeekParityChecker

###Wersja 0.11

NOWE

- poprawiono obsługe braku łączności z Internetem
- kilka innych drobnych poprawek
- zmiana numeracji wesji

BRAK

- pobierania planu i grup ze strony WI.

BUGI

- czasami występuję wyjątek NullPointerException w klasie WeekParityChecker

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
- czasami występuję wyjątek NullPointerException w klasie WeekParityChecker

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

- czasami przy braku połaczenia z Internetem zmiany wyrzucają NullPointerException
- nie wyswietla się ostatnia zmiana w planie
- zbyt powolne pobieranie daty na starcie 