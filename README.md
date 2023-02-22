Aplikacija nije deployana jer imamo memory leak koji nismo uspjeli rijesiti u klijentskom kodu zbog kojega 
se container deployan na heroku posluzitelju rusio. 

Aplikacije se pokrecu tako da u folder IzvorniKod/Backend izvrtite naredbu mvn clean install te pokrenete main
metodu u klasi Application, te u folderu IzvorniKod/Backend izvrtite naredbe npm install te npm start koja 
ce pokrenuti klijentsku aplikaciju na URL-u http://localhost:3000/. Aplikacija je spojena na Postgresql bazu na
heroku posluzitelju te su pripremljeni korisnici s korisnickim imenima: userone, usertwo te company. Lozinka
za sve korisnike je "password". 