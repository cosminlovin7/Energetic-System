# Energetic-SystemV2
This is an updated version of the Energetic-System implemented during my Object-Oriented Programming course.

In implementarea mea am folosit conceptele de Factory si Singleton. Ele se regasesc in implementarea claselor Consumer,
Distributor, precum si in cea a claselor din folderul update.
Citirea fisierelor a fost realizata cu ajutorul lui jsonParser si este implementata in clasa InputLoader.
Toate datele de intrare sunt retinute intr-un obiect de tip Input.
Lista de consumator este retinuta intr-un Map<Integer, Consumer> unde integer reprezinta id-ul consumatorului si
consumer este un obiect din clasa Consumer ce contine informatiile din fisierul de intrare .json.
Asemenea am procedat si pentru lista de distribuitori.

Fiecare obiect din lista de update-uri lunare contine la randul lui, o lista de obiecte de tip CostChange( schimbarile
lunare ce sunt aduse pentru diferiti distribuitori ), si o lista de obiecte de tip NewConsume( consumatorii noi ce
urmeaza sa fie adaugati in lista de consumatori ).
Generarea obiectelor de tip CostChange si NewConsumer se realizeaza prin clasa factory UpdateFactory, iar generarea
obiectelor de tip Consumer si Distributor se realizeaza prin intermediul clasei factory MembersGenerator.

Clasa Consumer contine toate informatiile pe care le putem avea despre un client precum:
-id
-valoarea contractului
-daca are un distribuitor de energie sau nu
-daca este ingropat in datorii (bankrupt :)))
-daca are datorii la vreun distribuitor, si daca da, id-ul acestuia.
-etc.(Ele se pot observa mai bine in clasa Consumer, sunt descrise acolo fiecare setter si getter)

Clasa Distributor contine toate informatiile pe care le putem avea despre un distribuitor precum:
-id
-buget
-valoarea contractului
-costul de infrastructura si productie
-daca este ingropat in datorii sau nu
-o clasa interna Contracts, cu ajutorul careia vom retine un HashMap, dar si o lista(pentru output),ce contin toti
clientii ce au contract cu distribuitorul respectiv.

Simularea jocului se face prin intermediul unui obiect de tip Simulate( ce implementeaza un singleton ).
In clasa simulate, avem 3 metode.
Prima metoda, getBestContract(), dupa cum scrie si acolo, intoarce distribuitorul cu cel mai bun contract de pe piata.
Se porneste cu primul Distributor din lista de distributori care NU ESTE BANKRUPT!. Acesta se compara cu ceilalti ]
Distributor din lista care, la randul lor, NU SUTN BANKRUPT. Daca se gaseste vreounul care sa aiba un contract mai bun
se inlocuieste cu acela, si acesta va fi comparat cu restul in continuare.

initiate() -> simuleaza faza de inceput a jocului. Practic se simuleaza luna dinaintea primei luni din joc.
Ordinea operatiilor este cea recomandata pe forum:

se updateaza costul contractelor pentru fiecare distribuitor:
-> pentru fiecare distribuitor se calculeaza costul contractului in functie de cati clienti are folosind formulele
de pe site

se updateaza salariile cu metoda updateSalaries():
-> aceasta metoda parcurge toata lista de Consumers, si mareste bugetul cu monthlyIncome pentru toti consumatorii care
NU SUNT BANKRUPT.

se alege un contract cu metoda setContracts():
-> se parcurge fiecare consumator din lista de consumatori. Daca acestia nu au distribuitor si NU SUNT BANKRUPT SAU
nu mai au contract cu nimeni (remainedContractsMonth = 0) si NU SUNT BANKRUPT SAU distribuitorul lor este BANKRUPT,
atunci ei vor "semna" un contract nou cu cel mai bun distribuitor din luna aceea gasit cu functia getBestContract().

se platesc taxele prin intermediul metodelor consumerPayTaxes() si distributorPayTaxes().
la final se verifica daca vreun distributor are in lista lui de contracte un client care a falimentat. Daca da, acesta
este eliminat din joc.

La fel functioneaza si functia de simulateOneMonth doar ca aici inainte de a se updata costul contractelor pentru
fiecare distribuitor, sunt adaugati in lista de consumatori, noii consumatori din acea luna iar costul distribuitorilor
este updatat(DACA E CAZUL).

Jocul este simulat de numberOfTurns + 1 ori. (initiate() + simulateOneMonth * numberOfTurns).

Pentru output am folosit ObjectMapper. Am pus intr-un jsonArray toate obiectele de tip consumer si intr-un jsonArray
toate obiectele de tip distributor.
Aceste jsonArray-uri le-am pus intr-un LinkedHashMap, si am afisat LinkedHashMap-ul pentru a obtine un output identic
cu cel din ref.
jsonArray-urile sunt generate cu ajutorul clasei OutputGenerator care implementeaza 2 metode:
getJSONConsumers -> intoarce toti consumatorii intr-un jsonArray
getJSONDistributors -> intoarce toti distribuitorii intr-un jsonArray
