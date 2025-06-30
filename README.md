Library - správa knihovny (REST API)
Popis
CRUD aplikace zaměřená na správu malé knihovny.
Administrátoři mohou provádět CRUD operace nad knihami, autory, lokacemi (umístění knih), uživateli a výpůjčkami (rezervacemi).
Uživatelé mohou prohlížet dostupné knihy, autory, lokaci a provádět rezervace (v případě, že není aktuálně vypůjčena).
Projekt je založen na předchozí MVC verzi, ale vytvořen od začátku s důrazem na čistší, správnější a efektivnější architekturu, 
proto se nejedná o kopii 1:1. 
Stále ve fázi vývoje v aktuální fázi obsahuje všechny potřebné základní CRUD operace, 
nyní se zmaměřuji na přidávání novějších funkcí a posouvání projektu kupředu. Zabezpečení skrze spring Security 
a založeno HTTP Basic sec (pro účely snadného testování v Postmanu). Dále projet obsahuje jednoduché loggování prováděných operacecí skrze AOP.
Projekt obsahuje i menší množství UnitTestů(dodělávám). 
Projekt skrze obsahuje Swagger dokumentaci Spring WebMvc (po suptění v IDE dostupná na endpointu: "/swagger-ui.html")

Spuštění
Pro spuštění je potřeba:
Naklonovat repozitář do svého IDE.
Spustit SQL skript ve své databázi (doporučuji MySQL) - resources/dbscripts/dbsetupscript.sql
Spustit projekt v IDE.
Pro testování endpointů třeba využít např. Postman

Technologie
Java
Spring Boot, Spring Security
JPA/Hibernate, MySQL
JUnit, Mockito
Swagger (Spring WebMvc)
Použití / Cíl
Jedná se o hobby projekt, kde aplikuji naučené znalosti a neustále se zlepšuji.
