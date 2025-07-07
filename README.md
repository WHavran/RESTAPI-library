# Library - Správa knihovny (REST API)

## Popis

Tato CRUD aplikace slouží ke správě malé knihovny.

- Administrátoři mohou provádět CRUD operace nad:
  - knihami,
  - autory,
  - lokacemi (umístění knih),
  - uživateli,
  - výpůjčkami (rezervacemi).

- Uživatelé mohou:
  - prohlížet dostupné knihy, autory a lokace,
  - vytvářet rezervace knih (pokud nejsou aktuálně vypůjčeny)
  - aktualizovat své údaje

Projekt navazuje na předchozí MVC verzi, ale je vytvořen zcela od začátku s důrazem na:
- čistší a efektivnější architekturu,
- lepší návrhové vzory.

Projekt je stále ve vývoji. Aktuálně obsahuje všechny základní CRUD operace a pracuje se na dalších funkcionalitách.

## Zabezpečení

- Zabezpečení je řešeno pomocí Spring Security.
- Používá se HTTP Basic Authentication (pro snadné testování např. v Postmanu).

## Další funkce

- AOP pro jednoduché logování prováděných operací.
- Swagger dokumentace dostupná na:  
  `http://localhost:8080/swagger-ui.html`
- Základní jednotkové testy (další jsou postupně doplňovány).

## Spuštění

1. Naklonuj repozitář do svého IDE.
2. Spusť SQL skript v databázi (doporučeno MySQL):  
   `src/main/resources/dbscripts/dbsetupscript.sql`
3. Spusť aplikaci přes IDE nebo příkazovou řádku.
4. Pro testování API můžeš použít např. Postman.

## Použité technologie

- Java
- Spring Boot, Spring Security
- JPA / Hibernate, MySQL
- JUnit, Mockito
- Swagger (Spring WebMvc)

## Účel

Jedná se o hobby projekt, ve kterém:
- uplatňuji své znalosti,
- neustále se učím a zlepšuji,
- experimentuji s architekturou a návrhem REST API.
