# Informacje wstępne

## Dla kogo jest warsztat
Dla osób chcących się zapoznać z podstawami pracy w Javie z dokumentową bazą danych MongoDB. Poziom podstawowy, warsztat **PRAKTYCZNY**.

---

## Przygotowanie
Co należy przygotować przed przystąpieniem do warsztatu

### Należy wiedzieć:
* podstawy programowania w Javie
  * testy jednostkowe
  * zależności mvn i uruchamianie aplikacji oraz testów na mvn z IDE
  * (opcjonalnie) Lombok  
* podstawy baz danych (generalna idea, SQL nie jest wymagany)
* format JSON (przydatny link: https://www.youtube.com/watch?v=haYYypSnOTY)

### Należy mieć zainstalowane:
* Java 11+ (jdk)
* Maven
* IntelliJ IDEA

### Można zainstalować:
Mocno polecane
* MongoDB z oficjalnej strony (https://docs.mongodb.com/guides/server/install/), dla swojego systemu operacyjnego, Server + Shell

Polecane - dowolne IDE do MongoDB
* np. preferowany Robo 3T - https://robomongo.org/download - wystarczy Robo 3T, bez Studio 3T
* Atlas Mongo z oficjalnej strony mongoDB (na własną rękę)

### Warto sprawdzić czy:
* projekt się kompiluje i buduje (zależności `mvn` się dociągają), przykładowy test się odpala
* proces mongoDB działa w systemie (nazywa się 'mongod')
* można podłączyć się do mongo na domyślnych ustawieniach (localhost, domyślny port)
    * z wybranego IDE
    * z konsoli poleceniem 'mongo'
    
### Dodatkowa wiedza/materiały

* poprzedni warsztat z "czystego" MongoDB: https://github.com/jkotor/inetum_academy_mongo

### Fallback
Większość warsztatu będzie opierała się na testach jednostkowych i bazie "w pamięci", więc nawet w przypadku nie działającego 
procesu 'mongod' można będzie rozwiązywać zadania.
 

---

## Plan warsztatu
1. Krótkie wprowadzenie (noSQL, bazy dokumentowe, kolekcje i dokumenty, podobieństwa i różnice z SQL)
2. Zadania praktyczne* bazy danych i kolekcje (dodawanie, usuwanie, listowanie)
    * dokumenty w kolekcjach, CRUD (Create, Read, Update, Delete)
    * wyszukiwanie dokumentów, podstawy (jak SELECT w SQL)
    *  Używane technologie (jeśli starczy czasu przerobimy oba podejścia) 
        * Klasyczne java-mongo-driver
        * Spring Data Mongo
4. Podsumowanie
5. Krótkie Q&A  

---

### Procesowanie w git (przydatne dla osób z małą znajomością git-a)
Proponuję następujące podejście w trakcie warsztatu:
* każdy uczestnik pracuje jedynie na lokalnym repozytorium, zdalne służy jako punkt wejściowy do zadań
* po rozwiązaniu każdej części zadań uczestnik commituje lokalnie swoje zmiany, np:
    ```
    git commit -m "rozwiązanie pierwszej partii zadan"
    ```
* następnie uczestnik pobiera kolejną część zadań (zmiany nie powinny powodować konfliktów), np:
    ```
    git pull
    ```
  i akceptujemy domyślne mergowanie branchy. To powinno wystarczyć jako minimum.

Oczywiście można też rewertować własne zmiany, zapisywać na innych branchach i robić różne sztuczki w gicie jeśli wiemy co robimy. 
W trakcie warsztatu nie będziemy wspólnie rozwiązywać ewentualnych problemów gita :). 

W ostateczności rescue command (https://stackoverflow.com/questions/1146973/how-do-i-revert-all-local-changes-in-git-managed-project-to-previous-state):
  ```
  git reset --hard
  ```
    
