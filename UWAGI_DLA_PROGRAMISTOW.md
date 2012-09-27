##UWAGI dla programistów:
###1.Emulatory Androida mają znany błąd, w którym metoda updateAppWidget() nie zachowuję się poprawnie.

Źródło [TUTAJ]( http://code.google.com/p/android/issues/detail?id=8889)
i z wlasnego doswiadczenia.

Niestety pomimo tego , że jest tam napisane iż w emulatorach z Androidem >= 2.2 działa wszystko w porządku, wg mnie tak nie jest (testowane na 2.3.3).

*INSTRUKCJA JAK URUCHAMIAC WIDGET NA EMULATORZE:*

1. Uruchom czysta instancje emulator ( wipe user data).
2. Zainstalujcie cokolwiek ( np widget).
3. Zamknijcie emulator.
4. Wystartujcie go ponownie lecz BEZ SNAPSHOTA i BEZ WPIE USER DATA, po prostu zwyk�y start.  
(trzeba odznaczyc odpowiednie dwie opcje w okienku pojawiajacym sie po klikni�ciu na start, jedyne co powinno by� zaznaczone
to "Save to snapshot").



###2.Napisy wrzucajcie tylko do strings.xml, nie piszcie ich bezpośrednio w kodzie.
Jeżeli po sciagnieciu z githuba zmieni się kodowanie, lub chcecie przetłumaczyć apke można je łatwo poprawić z jednego miejsca.
Ponado jest  to dobra praktyka. Nie dotyczy to oczywiście logów. (Trzeba zrobić porządek w aplikacji jeżeli jakies napisy jeszcze zostały).

###3.Wszelkie zmiany w klasach MadWidgetProvider oraz UpdateWidgetService prosze konsultować ze mną.
Są to "delikatne" klasy i nawet teoretycznie logiczna zmiana może powodowac, że cały widget przestanie się poprawnie odswieżać.