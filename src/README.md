# Zadaća 1 – Mirnesa Pobrić

## Opis rješenja
Program modelira inženjere koristeći **klase, nasljeđivanje i interfejse** u Kotlinu.  

- **Osoba** – interfejs sa metodama Identitet() i Titula().  
- **Inzenjer** – baza klasa sa zajedničkim atributima: ime, prezime, titula, godine iskustva i ekspertize.  
- **Specijalizirane klase**: SoftverskirInzenjer i InzenjerElektrotehnike dodaju specifične atribute (brojProjekata i brojCertifikata).  
- **Obrada podataka**:
  - grupisanjePoEkspertizama() – grupiše inženjere po ekspertizama sa više od 5 godina iskustva (**fold**).  
  - najiskusnijiPoTipu() – pronalazi najiskusnijeg inženjera u svakoj kategoriji (**reduce**).  
  - ukupneVrijednosti() – računa ukupan broj projekata i certifikata (**aggregate**).

## Upotreba AI alata
- AI (ChatGPT) korišten za **objašnjenje funkcija** i koncepta fold/reduce/aggregate.  
- AI je korišten samo kao pomoć u učenju, ne za generisanje kompletnog koda.

## Primjer ispisa programa


=== LISTA SVIH INŽENJERA ===
1. Amina Hodžić - Softverski inženjer - 8 godina - Ekspertize: Kotlin, Android, Java - Projekti: 15
2. Lejla Karić - Softverski inženjer - 12 godina - Ekspertize: Java, Spring, Kotlin - Projekti: 25
3. Tarik Mujić - Inženjer elektrotehnike - 10 godina - Ekspertize: Elektronika, Mikrokontroleri - Certifikati: 8
...

=== GRUPISANJE PO EKSPERTIZAMA (>5 god iskustva) ===
Ekspertiza: Kotlin (2 inženjera)
  - Amina Hodžić (8 godina)
  - Lejla Karić (12 godina)

=== NAJISKUSNIJI PO TIPU ===
Softverski inženjer: Lejla Karić - 12 godina iskustva
Inženjer elektrotehnike: Nermin Softić - 15 godina iskustva

=== UKUPNE VRIJEDNOSTI ===
Ukupan broj projekata i certifikata: 87
  - Projekti (softverski): 60
  - Certifikati (elektrotehnički): 27

