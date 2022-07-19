import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

@SuppressWarnings("unchecked")
public class Company{
    private String username;
    private String password;
    private String companyName;
    private int totalDonation;
    //Bilgiler sıralı bir şekilde tutulsun diye TreeMap kullanılmıştır.
    private TreeMap<String, Dog> dogs;
    private TreeMap<String, Cat> cats;
    private TreeMap<String, Bird> birds;
    private TreeMap<String, Person> persons;
    private Source source;
    private ArrayList<Donation> donations;
    private ArrayList<Animal> animalsOwnedByPerson;

    public Company(String companyName){
        this.companyName = companyName;
        dogs = new TreeMap<>();
        cats = new TreeMap<>();
        birds = new TreeMap<>();
        persons = new TreeMap<>();
        donations = new ArrayList<>();
        animalsOwnedByPerson = new ArrayList<>();
        readFromFile();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalDonation() {
        return totalDonation;
    }

    public void showCurrentBalance(){
        System.out.println("Güncel Bakiye: " + getTotalDonation());
    }

    public String toString(){
        String intro = "";
        intro += "---------------- Şirket Bilgileri ----------------\n" + "• Şirket Adı: " + companyName;
        intro += "\n• " + dogs.size()  + " adet yeni sahibini bekleyen köpek bulunmaktadır.";
        intro += "\n• " + cats.size()  + " adet yeni sahibini bekleyen kedi bulunmaktadır.";
        intro += "\n• Sistemde" + persons.size()  + " adet kişi kayıtlıdır.";
        return intro;
    }

    private void readFromFile(){
        try{
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream("company.bin"));
            username = (String) objIn.readObject();
            password = (String) objIn.readObject();
            companyName = (String) objIn.readObject();
            totalDonation = (Integer) objIn.readObject();
            dogs = (TreeMap<String, Dog>) objIn.readObject();
            cats = (TreeMap<String, Cat>) objIn.readObject();
            birds = (TreeMap<String, Bird>) objIn.readObject();
            persons = (TreeMap<String, Person>) objIn.readObject();
            source = (Source) objIn.readObject();
            donations = (ArrayList<Donation>) objIn.readObject();
            for(Donation donation : donations)
                donation.setDonor(findPerson(donation.getDonor().getID())); //bağışları kişilerle eşliyorum
            animalsOwnedByPerson = (ArrayList<Animal>) objIn.readObject();
            for(Animal animal : animalsOwnedByPerson)
                animal.setOwner(findPerson(animal.getOwner().getID())); //hayvanları kişilerle eşliyorum

            //Counter'ı set etmek için geçici bir hayvan oluşturma
            Dog dog = new Dog(0, "0", "0", "0", 0);
            dog.setCounter(dogs.size() + cats.size() + birds.size() + animalsOwnedByPerson.size() + 1);
            objIn.close();
        }catch (Exception e){
            System.out.println("Dosyadan okurken bir hata ile karşılaşıldı. Sistem kendini kapattı.\n");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void writeToFile(){
        try{
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("company.bin"));
            objOut.writeObject(username);
            objOut.writeObject(password);
            objOut.writeObject(companyName);
            objOut.writeObject(totalDonation);
            objOut.writeObject(dogs);
            objOut.writeObject(cats);
            objOut.writeObject(birds);
            objOut.writeObject(persons);
            objOut.writeObject(source);
            objOut.writeObject(donations);
            objOut.writeObject(animalsOwnedByPerson);
            objOut.close();
        }catch (Exception e){
            System.out.println("Dosyaya yazarken bir hata oluştu.");
            e.printStackTrace();
        }
    }

    /* Hayvan Methodları */
    public <T extends Animal> T findAnimal(String ID){
        if(dogs.get(ID) != null){
            return (T) dogs.get(ID);
        }else if(cats.get(ID) != null){
            return (T) cats.get(ID);
        }else if(birds.get(ID) != null){
            return (T) birds.get(ID);
        }
        return null;
    }

    public <T extends Animal> void  addAnimal(T animal){
        if(animal instanceof Dog){ // animal instanceof Dog == animal.getClass().getName().compareTo("Dog") == 0
            Dog dog = (Dog) animal;
            dogs.put(dog.getID(), dog);
            System.out.println("\nBarınağa yeni bir köpek eklendi. Barınaktaki köpek sayısı: " + dogs.size() + "\n");
        }else if(animal instanceof Cat){
            Cat cat = (Cat) animal;
            cats.put(cat.getID(), cat);
            System.out.println("\nBarınağa yeni bir kedi eklendi. Barınaktaki kedi sayısı: " + cats.size() + "\n");
        }else if(animal instanceof Bird){
            Bird bird = (Bird) animal;
            birds.put(bird.getID(), bird);
            System.out.println("\nBarınağa yeni bir kuş eklendi. Barınaktaki kuş sayısı: " + birds.size() + "\n");
        }else{
            System.out.println("Yazmış olduğunuz ID'ye sahip hayvan bulunamadı!!");
        }
    }

    public Dog createDog() throws Exception{
        System.out.println("Yeni köpeğin sırasıyla yaşını, türünü, sağlık durumunu(Sağlıklı - Yaralı - Hasta) ve yediği günlük mama miktarını " +
                " aralarında bir boşluk olacak şekilde yazınız: ");

        String line = Main.input.nextLine();
        String[] words = line.split(" ");

        return new Dog(Integer.parseInt(words[0]), words[1], words[2], Main.dateFormatter.format(new Date()), Integer.parseInt(words[3]));
    }

    public void showDogs(){
        if(dogs.size() == 0){
            System.out.println("Barınakta köpek BULUNMAMAKTADIR.");
            return;
        }
        System.out.println("----------------------------------------- Barınaktaki Köpeklerimiz -----------------------------------------");
        for(Dog dog : dogs.values())
            System.out.println(dog);
        System.out.println("------------------------------------------------------------------------------------------------------------\n");
    }

    public Cat createCat() throws Exception{
        System.out.println("Yeni kedinin sırasıyla yaşını, türünü, sağlık durumunu(Sağlıklı - Yaralı - Hasta) ve yediği günlük mama miktarını " +
                " aralarında bir boşluk olacak şekilde yazınız: ");

        String line = Main.input.nextLine();
        String[] words = line.split(" ");

        return new Cat(Integer.parseInt(words[0]), words[1], words[2], Main.dateFormatter.format(new Date()), Integer.parseInt(words[3]));
    }

    public void showCats(){
        if(cats.size() == 0){
            System.out.println("Barınakta kedi BULUNMAMAKTADIR.");
            return;
        }
        System.out.println("----------------------------------------- Barınaktaki Kedilerimiz -----------------------------------------");
        for(Cat cat : cats.values())
            System.out.println(cat);
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
    }

    public Bird createBird() throws Exception{
        System.out.println("Yeni kuşun sırasıyla yaşını, türünü, sağlık durumunu(Sağlıklı - Yaralı - Hasta) ve yediği günlük yem miktarını " +
                " aralarında bir boşluk olacak şekilde yazınız: ");

        String line = Main.input.nextLine();
        String[] words = line.split(" ");

        return new Bird(Integer.parseInt(words[0]), words[1], words[2], Main.dateFormatter.format(new Date()), Integer.parseInt(words[3]));
    }

    public void showBirds(){
        if(birds.size() == 0){
            System.out.println("Barınakta kuş BULUNMAMAKTADIR.");
            return;
        }
        System.out.println("----------------------------------------- Barınaktaki Kuşlarımız -----------------------------------------");
        for(Bird bird : birds.values())
            System.out.println(bird);
        System.out.println("----------------------------------------------------------------------------------------------------------\n");
    }

    /* Kişi Methodları */
    private Person createPerson() throws Exception{
        System.out.println("Eklemek istediğiniz yeni kişinin sırasıyla adını, soyadını, T.C. kimlik numarasını ve telefon numarasını yazınız: ");

        String line = Main.input.nextLine();
        String[] words = line.split(" ");

        return new Person(words[0], words[1], words[2], words[3]);
    }

    private void addPerson(Person person){
        persons.put(person.getID(), person);
        System.out.println("\nSisteme yeni bir kişi eklendi. Sistemdeki kayıtlı kişi sayısı: " + persons.size() + "\n");
    }

    private Person findPerson(String ID){
        return persons.get(ID);
    }

    public void showPersons(){
        if(persons.size() == 0){
            System.out.println("Sistemde kişi kaydı BULUNMAMAKTADIR.");
            return;
        }
        System.out.println("----------------------------------------- Sistemde Kayıtlı Kişiler -----------------------------------------");
        for(Person person : persons.values())
            System.out.println(person);
    }

    /* Bağış Methodları */
    public void getDonation() throws Exception{
        Person donor;
        System.out.println("Bağış yapan kişinin T.C. kimlik numarasını yazınız: ");
        String ID = Main.input.nextLine();
        if((donor = findPerson(ID)) == null){
            System.out.println("Bu kişi sistemde kayıtlı olmadığı için kayıt etmeniz gerekmektedir.");
            donor = createPerson();
            addPerson(donor);
        }

        System.out.println("Yapmak istediğiniz bağış miktarını giriniz: ");
        String line = Main.input.nextLine();
        donations.add(new Donation(donor, Integer.parseInt(line), Main.dateFormatter.format(new Date())));
        donor.setTotalDonation(donor.getTotalDonation() + Integer.parseInt(line));
        totalDonation += Integer.parseInt(line);
        System.out.println(donor.getName() + " " + donor.getSurname() + " adına " + Integer.parseInt(line) + " TL değerinde bağış sisteme kaydedildi.");
    }

    public void showDonations(){
        if(donations.size() == 0){
            System.out.println("Hiç bağış BULUNMAMAKTADIR.");
            return;
        }
        System.out.println("------------------------------ Yapılan Bağışlar ------------------------------");
        for (Donation donation : donations) {
            System.out.println("Bağış Yapan Kişinin Adı Soyadı: " + donation.getDonor().getName() + " " + donation.getDonor().getSurname() + " | T.C.: " +
                    donation.getDonor().getID() + " | Tutar: " + donation.getAmount() + " TL | Tarih: " + donation.getDonateDate());
        }
        System.out.println("Güncel bakiye: " + totalDonation);
    }

    /* Kaynak Methodları */
    public void addSource() throws Exception{
        int answer, number;
        boolean flag = true;

        do {
            System.out.println("• Köpek maması satın almak için 1'e,\n• Kedi maması satın almak için 2'e,\n• Kuş yemi satın almak için 3'e basınız: ");
            answer = Main.input.nextInt();
            Main.input.nextLine(); //int okumadaki enter'ı okuyor
            switch (answer) {
                case 1:
                    System.out.println("1 köpek maması " + source.getDogFood().getAmount() + " gramdır ve " + source.getDogFood().getPrice() + " TL'dir." +
                            " Şu anki bakiyeniz: " + totalDonation + " TL. Kaç adet köpek maması almak istiyorsunuz? ");
                    number = Main.input.nextInt();
                    Main.input.nextLine(); //int okumadaki enter'ı okuyor
                    if (totalDonation >= number * source.getDogFood().getPrice()) {
                        source.addDogFood(number);
                        totalDonation -= number * source.getDogFood().getPrice();
                        System.out.println(number + " adet köpek maması alındı ve şu an depoda " + source.getDogFoodAmount() + " gram köpek maması bulunmaktadır. " +
                                "Güncel bakiyeniz: " + totalDonation + " TL.");
                    } else {
                        System.out.println("Maalesef bakiyeniz yetersiz olduğu için satın alım GERÇEKLEŞMEDİ!!");
                    }
                    flag = false;
                    break;
                case 2:
                    System.out.println("1 kedi maması " + source.getCatFood().getPrice() + " gramdır ve " + source.getCatFood().getPrice() + " TL'dir" +
                            " Şu anki bakiyeniz: " + totalDonation + " TL. Kaç adet kedi maması almak istiyorsunuz?");
                    number = Main.input.nextInt();
                    Main.input.nextLine(); //int okumadaki enter'ı okuyor
                    if (totalDonation >= number * source.getCatFood().getPrice()) {
                        source.addCatFood(number);
                        totalDonation -= number * source.getCatFood().getPrice();
                        System.out.println(number + " adet kedi maması alındı ve şu an depoda " + source.getCatFoodAmount() + " gram kedi maması bulunmaktadır. " +
                                "Güncel bakiyeniz: " + totalDonation + " TL.");
                    } else {
                        System.out.println("Maalesef bakiyeniz yetersiz olduğu için satın alım GERÇEKLEŞMEDİ!!");
                    }
                    flag = false;
                    break;
                case 3:
                    System.out.println("1 kuş yemi " + source.getBirdFood().getPrice() + " gramdır ve " + source.getBirdFood().getPrice() + " TL'dir" +
                            " Şu anki bakiyeniz: " + totalDonation + " TL. Kaç adet kedi maması almak istiyorsunuz?");
                    number = Main.input.nextInt();
                    Main.input.nextLine(); //int okumadaki enter'ı okuyor
                    if (totalDonation >= number * source.getBirdFood().getPrice()) {
                        source.addBirdFood(number);
                        totalDonation -= number * source.getBirdFood().getPrice();
                        System.out.println(number + " adet kuş yemi alındı ve şu an depoda " + source.getBirdFoodAmount() + " gram kedi maması bulunmaktadır. " +
                                "Güncel bakiyeniz: " + totalDonation + " TL.");
                    } else {
                        System.out.println("Maalesef bakiyeniz yetersiz olduğu için satın alım GERÇEKLEŞMEDİ!!");
                    }
                    flag = false;
                    break;
                default:
                    System.out.println("Yanlış bir tuşlama yaptınız. Tekrar deneyiniz..");
                    break;
            }
        }while (flag);
    }

    public void showSource(){
        System.out.println("--------------- Depoda Bulunan Köpek Maması, Kedi Maması ve Kuş Yemi Miktarları ---------------");
        System.out.println("• Köpek Maması: " + source.getDogFoodAmount() + " gr\n• Kedi Maması: " + source.getCatFoodAmount() + " gr\n• Kuş Yemi: " +
                source.getBirdFoodAmount());
    }

    /* Hayvan Sahiplendirme Methodları */
    public void makeOwner() throws Exception{
        boolean flag = true;
        int ID, answer;
        Person owner;
        do{
            System.out.println("• Köpek sahiplendirmek için 1'e,\n• Kedi sahiplendirmek için 2'e,\n• Kuş sahiplendirmek için 3'e basınız: ");
            answer = Main.input.nextInt();
            Main.input.nextLine(); //int okumadaki enter'ı okuyor
            switch (answer){
                case 1:
                    showDogs();
                    System.out.println("Sahiplendirmek istediğiniz köpeğin ID'sini giriniz: ");
                    ID = Main.input.nextInt();
                    Main.input.nextLine(); //int okumadaki enter'ı okuyor
                    Dog dog = findAnimal(String.format("%04d", ID));
                    if(dog == null){
                        System.out.println("Böyle bir köpek BULUNAMADI!!");
                    }else{
                        System.out.println("Sahiplenecek olan kişinin T.C. kimlik numarasını giriniz: ");
                        owner = findPerson(Main.input.nextLine());
                        if(owner == null){
                            System.out.println("Bu kişi sistemde kayıtlı olmadığı için kayıt etmeniz gerekmektedir.");
                            owner = createPerson();
                            addPerson(owner);
                        }
                        if(owner.getAnimalID() == null){
                            dog.setOwnershipDate(Main.dateFormatter.format(new Date()));
                            dog.setOwner(owner);
                            owner.setAnimalID(dog.getID(), "Köpek");
                            animalsOwnedByPerson.add(dog);
                            dogs.remove(dog.getID());
                            System.out.println(owner.getName() + " " + owner.getSurname() + " adlı kişi, cinsi " + dog.getKind() + " olan bir köpeği sahiplendi.");
                        }else{
                            System.out.println("Bu kişi zaten bir hayvana sahip!!");
                        }

                    }
                    flag = false;
                    break;
                case 2:
                    showCats();
                    System.out.println("Sahiplendirmek istediğiniz kedinin ID'sini giriniz: ");
                    ID = Main.input.nextInt();
                    Main.input.nextLine(); //int okumadaki enter'ı okuyor
                    Cat cat = findAnimal(String.format("%04d", ID));
                    if(cat == null){
                        System.out.println("Böyle bir kedi BULUNAMADI!!");
                    }else{
                        System.out.println("Sahiplenecek olan kişinin T.C. kimlik numarasını giriniz: ");
                        owner = findPerson(Main.input.nextLine());
                        if(owner == null){
                            System.out.println("Bu kişi sistemde kayıtlı olmadığı için kayıt etmeniz gerekmektedir.");
                            owner = createPerson();
                            addPerson(owner);
                        }
                        if(owner.getAnimalID() == null){
                            cat.setOwnershipDate(Main.dateFormatter.format(new Date()));
                            cat.setOwner(owner);
                            owner.setAnimalID(cat.getID(), "Kedi");
                            animalsOwnedByPerson.add(cat);
                            cats.remove(cat.getID());
                            System.out.println(owner.getName() + " " + owner.getSurname() + " adlı kişi, cinsi " + cat.getKind() + " olan bir kediyi sahiplendi.");
                        }else{
                            System.out.println("Bu kişi zaten bir hayvana sahip!!");
                        }
                    }
                    flag = false;
                    break;
                case 3:
                    showBirds();
                    System.out.println("Sahiplendirmek istediğiniz kuşun ID'sini giriniz: ");
                    ID = Main.input.nextInt();
                    Main.input.nextLine(); //int okumadaki enter'ı okuyor
                    Bird bird = findAnimal(String.format("%04d", ID));
                    if(bird == null){
                        System.out.println("Böyle bir kuş BULUNAMADI!!");
                    }else{
                        System.out.println("Sahiplenecek olan kişinin T.C. kimlik numarasını giriniz: ");
                        owner = findPerson(Main.input.nextLine());
                        if(owner == null){
                            System.out.println("Bu kişi sistemde kayıtlı olmadığı için kayıt etmeniz gerekmektedir.");
                            owner = createPerson();
                            addPerson(owner);
                        }
                        if(owner.getAnimalID() == null){
                            bird.setOwnershipDate(Main.dateFormatter.format(new Date()));
                            bird.setOwner(owner);
                            owner.setAnimalID(bird.getID(), "Kuş");
                            animalsOwnedByPerson.add(bird);
                            birds.remove(bird.getID());
                            System.out.println(owner.getName() + " " + owner.getSurname() + " adlı kişi, cinsi " + bird.getKind() + " olan bir köpeği sahiplendi.");
                        }else{
                            System.out.println("Bu kişi zaten bir hayvana sahip!!");
                        }
                    }
                    flag = false;
                    break;
                default:
                    System.out.println("Yanlış bir tuşlama yaptınız. Tekrar deneyiniz..");
                    break;
            }
        }while(flag);
    }

    public void showAnimalsOwnedByPerson(){
        if(animalsOwnedByPerson.size() == 0){
            System.out.println("Sahiplenilmiş bir hayvan BULUNMAMAKTADIR.");
            return;
        }
        System.out.println("---------------------- Sahiplenilmiş Hayvanlar ------------------------------------ ");
        for(Animal animal : animalsOwnedByPerson)
            System.out.println(animal + " | Türü: " + determineWhichAnimal(animal.getClass().getName()));
        System.out.println("-----------------------------------------------------------------------------------\n");
    }

    public String determineWhichAnimal(String whichAnimal){
        if(whichAnimal.compareTo("Dog") == 0){
            return "Köpek";
        }else if(whichAnimal.compareTo("Cat") == 0){
            return "Kedi";
        }else if(whichAnimal.compareTo("Bird") == 0){
            return "Kuş";
        }
        return "Hata";
    }

    /* Hayvan Besleme Methodu */
    public <T extends Animal> void feedAnimal(T animal){
        if(animal instanceof Dog){
            if(source.getDogFoodAmount() >= animal.getFoodItAte()){
                System.out.println(animal.getID() + " ID'li köpek beslendi.");
                System.out.println("Beslemeden Önce Depodaki Köpek Maması Miktarı : " + source.getDogFoodAmount() + " gr.");
                source.setDogFoodAmount(source.getDogFoodAmount() - animal.getFoodItAte());
                System.out.println("Beslemeden Sonra Depodaki Köpek Maması Miktarı : " + source.getDogFoodAmount() + " gr.");
            }else{
                System.out.println("Depoda yeterli miktarda köpek maması BULUNMAMAKTADIR. Bu yüzden beslemek istediğiniz köpek BESLENEMEDİ!!");
                System.out.println("Olması gereken: " + animal.getFoodItAte() + " gr. Depoda bulunan: " + source.getDogFoodAmount() + " gr.\n");
            }
        }else if(animal instanceof Cat){
            if(source.getCatFoodAmount() >= animal.getFoodItAte()){
                System.out.println(animal.getID() + " ID'li kedi beslendi.");
                System.out.println("Beslemeden Önce Depodaki Kedi Maması Miktarı : " + source.getCatFoodAmount() + " gr.");
                source.setCatFoodAmount(source.getCatFoodAmount() - animal.getFoodItAte());
                System.out.println("Beslemeden Sonra Depodaki Kedi Maması Miktarı : " + source.getCatFoodAmount() + " gr.");
            }else{
                System.out.println("Depoda yeterli miktarda kedi maması BULUNMAMAKTADIR. Bu yüzden beslemek istediğiniz kedi BESLENEMEDİ!!");
                System.out.println("Olması gereken: " + animal.getFoodItAte() + " gr. Depoda bulunan: " + source.getCatFoodAmount() + " gr.\n");
            }
        }else if(animal instanceof Bird){
            if(source.getBirdFoodAmount() >= animal.getFoodItAte()){
                System.out.println(animal.getID() + " ID'li kuş beslendi.");
                System.out.println("Beslemeden Önce Depodaki Kuş Yemi Miktarı : " + source.getBirdFoodAmount() + " gr.");
                source.setBirdFoodAmount(source.getBirdFoodAmount() - animal.getFoodItAte());
                System.out.println("Beslemeden Sonra Depodaki Kuş Yemi Miktarı : " + source.getBirdFoodAmount() + " gr.");
            }else{
                System.out.println("Depoda yeterli miktarda kuş yemi BULUNMAMAKTADIR. Bu yüzden beslemek istediğiniz kuş BESLENEMEDİ!!");
                System.out.println("Olması gereken: " + animal.getFoodItAte() + " gr. Depoda bulunan: " + source.getBirdFoodAmount() + " gr.\n");
            }
        }
    }

    /* Hayvan Silme Methodları */
    public void removeDog(String ID){
        Dog dog = dogs.remove(ID);
        if(dog == null){
            System.out.println("Bu ID'ye sahip bir köpek BULUNAMADI.");
            return;
        }

        System.out.println(ID + " ID'li köpek başarılı bir şekilde barınaktan silindi.");
    }

    public void removeCat(String ID){
        Cat cat = cats.remove(ID);
        if(cat == null){
            System.out.println("Bu ID'ye sahip bir kedi BULUNAMADI.");
            return;
        }
        System.out.println(ID + " ID'li kedi başarılı bir şekilde barınaktan silindi.");
    }

    public void removeBird(String ID){
        Bird bird = birds.remove(ID);
        if(bird == null){
            System.out.println("Bu ID'ye sahip bir kuş BULUNAMADI.");
            return;
        }
        System.out.println(ID + " ID'li kuş başarılı bir şekilde barınaktan silindi.");
    }

    /* Bağış Silme Methodu */
    private Donation findDonation(int amount, String ID){
        for(Donation donation : donations){
            if(donation.getAmount() == amount && donation.getDonor().getID().compareTo(ID) == 0){
                return donation;
            }
        }
        return null;
    }

    public void removeDonation() throws Exception{
        //İlk eşleşeni siler
        System.out.println("Silmek istediğiniz bağışın tutarını ve bağışı yapan kişinin T.C.'sini yazınız:");
        String line = Main.input.nextLine();
        String[] words = line.split(" ");
        Donation donation = findDonation(Integer.parseInt(words[0]), words[1]);
        if(donation == null){
            System.out.println("Sistemde böyle bir bağış BULUNAMADI.");
            return;
        }

        if(totalDonation < donation.getAmount()){
            System.out.println("Sistemde yeterli bakiye olmadığı için şu anda bu bağış silinemiyor.");
            return;
        }

        donations.remove(donation);
        totalDonation -= donation.getAmount();
        donation.getDonor().setTotalDonation(donation.getDonor().getTotalDonation() - donation.getAmount());
        System.out.println("Bağış başarılı bir şekilde sistemden silindi ve bakiye güncellendi.");
    }

    /* Kişi Silme Methodu */
    public void removePerson(){
        System.out.println("Silmek istediğiniz kişinin T.C. numarasını giriniz: ");
        Person person = findPerson(Main.input.nextLine());
        if(person == null){
            System.out.println("Bu kişi sistemde BULUNAMADI.");
            return;
        }

        if(person.getAnimalID() != null){
            System.out.println("Bu kişi bir hayvana sahip olduğu için sistemden SİLEMEZSİNİZ.");
            return;
        }

        if(person.getTotalDonation() != 0){
            System.out.println("Sistemde bu kişiye ait bağış bulunduğu için sistemden SİLEMEZSİNİZ.");
            return;
        }

        persons.remove(person.getID());
        System.out.println(person.getID() + " T.C. kimlik numarasına sahip kişi sistemden başarılı bir şekilde silindi.");
    }

    /* Kullanıcı Adı ve Şifre Değiştirme Methodu */
    public void showUsernameAndPassword(){
        System.out.println("Kullanıcı adı: " + username + " | Şifre: " + password);
    }

    public void changeUsernameAndPassword(){
        System.out.println("Yeni kullanıcı adını ve şifreyi aralarında bir boşluk olacak şekilde giriniz: ");
        String line = Main.input.nextLine();
        String[] words = line.split(" ");
        setUsername(words[0]);
        setPassword(words[1]);
        System.out.println("Yeni kullanıcı adı ve şifre BAŞARIYLA değiştirildi.");
    }
}
