Projekta apraksts:
Vienkārša Java desktopa lietotne, kas ļauj ierakstīt savus izdevumus MYSQL datu bāzē!
Ļauj pievienot un dzēst izdevumus (aprakstu, peļņu un datumu), klasificēt tos (pēc datuma, peļņas summas vai individuālā identifikatora) un pat summēt izdevumu peļņu!

Prasības:
1. IntelliJ IDEA (vai... cita Java programma)
2. MYSQL client (Workbench 8.0 CE or 8.0 Command Line Client)
3. Platform independent MYSQL connector (https://dev.mysql.com/downloads/connector/j/)

Instrukcijas:
• Izveidojiet lietotnei MYSQL tabulu
 1. Atveriet savu MYSQL klientu
 2. Izveidojiet datu bāzi (piem.: CREATE DATABASE db_name; ) vai izmantojiet jau esošu (piem.: USE db_name; )
 3. Izveidojiet tabulu "expenses" , kā parādīts tālāk:
 IZVEIDOT TABULU expenses(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(250) NOT NULL,
  sum DECIMAL(10, 2),
  date DATETIME* NOT NULL, 

 *DATETIME nozīmē, ka datums būs jāieraksta šādā formātā: YYYY-MM-DD hh:mm:ss 

 • Rediģējiet URL, lietotāju un paroli
 1. Lietotņu mapē atrodiet mapi "src".
 2. Atveriet GUI.java
 3. Rediģējiet 25., 26., 27. rindiņu, lai iekļautu sava MYSQL servera URL (piemēram, jdbc:mysql://localhost:3306/db_name), MYSQL user (piem.: root) un MYSQL server password (vai, ja  nav iestatīta parole, atstājiet to kā " ")
 4. Rediģējiet 59., 60., 61. rindiņu, tapāt kā 3. darbībā
 5. Atveriet DatabaseManager.java
 6. Rediģējiet 7., 8., 9. rindiņu, tapāt kā 3. darbībā

 • Savienojiet Java lietotni ar MYQl datu bāzi, izmantojot savienotāju:
 • Izmantojot IntelliJ:
 1. Atveriet projekta .iml failu
 2. Atvērt File -> project structure -> modules - dependencies 
 3. Nospiediet "+" un pievienojiet MYSQL savienotāja .jar failu
 4. Nospiediet “Apply” un “OK”.
 5. Saglabājiet izmaiņas

Screenshots:
![Izdreg_scrn](https://github.com/user-attachments/assets/633abd94-4baf-42b1-be19-0d70497a6924)
![izdreg_scrnmain](https://github.com/user-attachments/assets/10860adc-dc52-452d-8826-57d2b0c465fa)

