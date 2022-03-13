Rachiteanu Robert-Alexandru
322CBa
 
Main:
In functia main se apeleaza instanta de store, iar cu un reader se citesc pe 
rand comenzile date ca input. Cu ajutorul unui switch care verifica primul
cuvant din comanda se verifica pe ce face match si se apeleaza functia 
corespunzatoare odata cu ceilalti parametrii. In cazul in care comanda este
"exit" sau "quit" se iese din switch, programul incheindu-se

Store:
Aceasta clasa are o singura instanta, aici implementandu-se un Design Pattern
de tipul Singleton. Se initializeaza ArrayList-urile "currencies", "products",
"manufacturers", respectiv "discounts" pentru a fi folosite pe parcursul temei.
In interiorul metodei "readCsv", pe langa prelucrarea campurilor din fisierul
csv dat ca input cu ajutorul altor metode si introducerea acestora in 
ArrayList-urile corespunzatoare, se verifica tipul de moneda folosita in csv 
si se modifica preturile corespunzator. Pentru citire am folosit apache cum
a fost cerut pentru bonus. Restul metodelor au fost implementate conform 
principiilor temei, iar de asemenea au fost create metode pentru a asigura 
lizibilitatea codului din main(Ex: "listDiscounts()",
"listProductByManufacturer()", "showManufacturers()", etc.). Functiile care 
creeaza noi instante (Ex: "createCurrency") de asemenea adauga noile obiecte
in listele corespunzatoare acestora.

Product:
Clasa "Product" are implementat un Design Pattern de tip Builder. Am creat o 
clasa interna "Builder" in care se scriu Setter-ii, a carei instante se da ca
parametru Constructer-ului clasei astfel initializandu-se variabilele acesteia.

**************************
Mentiuni:
- S-au folosit in cadrul tuturor claselor realizate Getter-i si Setter-i;

- Au fost inplementate toate exceptiile cerute (Ex: "DiscountNotFoundException",
"DuplicateProductException", "NegativePriceException", etc.); 
