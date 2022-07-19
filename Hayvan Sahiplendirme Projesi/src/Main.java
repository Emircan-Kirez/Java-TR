/* Emircan KİREZ - Ch3rry */
/* Son Güncelleme: 19/07/2022 */
/*
----------------------- BİLGİLENDİRME --------------------------
• Projeye geçmeden önce README.md dosyasını okumanız tavsiye edilmektedir.
• Projeyle alakalı herhangi bir soru ve öneri için "emircan200123@hotmail.com" mail adresi üzerinden, konu başlığına "Github | Java-TR / Hayvan Sahiplendirme Sistem Projesi"
yazarak benimle iletişime geçebilirsiniz.
*/

import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Scanner input = new Scanner(System.in); //kullanıcıdan input almak için kullanılan Scanner nesnesi
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
    public static void main(String[] args) {
        Company company = new Company("Hayvan Barınak A.Ş.");
        login(company);
        menu(company);
    }

    public static void login(Company company){
        System.out.println("Sisteme kullanabilmek için öncelikle giriş yapmalısınız. 3 adet hakkınız bulunmaktadır.");
        int i = 0;
        while(i < 3){
            try{
                System.out.println("Kullanıcı adını ve şifreyi giriniz (Case Sensitive):");
                String line = input.nextLine();
                String[] words = line.split(" ");
                if(company.getUsername().compareTo(words[0]) == 0 && company.getPassword().compareTo(words[1]) == 0){
                    System.out.println("BAŞARILI\nSisteme aktarılıyorsunuz...");
                    TimeUnit.SECONDS.sleep(2); //2 saniye bekletme - gerçeklik için
                    return;
                }else{
                    System.out.println("BAŞARISIZ");
                    i++;
                }

            }catch (Exception e){
                System.out.println("Bir hata oluştu. Hata sebebi:\n " + e);
            }
        }

        System.out.println("Sistem kendini kapattı.");
        System.exit(0);
    }

    public static void menu(Company company){
        int option, answer;
        boolean flag = true;
        Dog dog;
        Cat cat;
        Bird bird;

        do{
            try{
                System.out.println("----------------------------- MENU ---------------------------\n• Barınağa hayvan eklemek için 1'e,\n• Bağış eklemek için 2'e," +
                        "\n• Kaynak eklemek için 3'e,\n• Hayvan sahiplendirmek için 4'e,\n• Hayvan beslemek için 5'e,\n• Barınaktan hayvan silmek için 6'a," +
                        "\n• Bağış silmek için 7'e,\n• Kişi silmek için 8'e,\n• Barınaktaki hayvanları görüntülemek için 9'a," +
                        "\n• Sahiplendirilmiş hayvanları görüntülemek için 10'a,\n• Sistemde kayıtlı olan kişileri görüntülemek için 11'e," +
                        "\n• Bağışları ve güncel bakiyeyi görüntülemek için 12'e,\n• Kullanıcı adı ve şifreyi görüntülemek için 13'e," +
                        "\n• Kullanıcı adı ve şifreyi değiştirmek için 14'e,\n• Sistemi kapatmak için 0'a basınız:");
                option = input.nextInt();
                input.nextLine(); //int okumadaki enter'ı okuyor
                switch (option){
                    case 0:
                        System.out.println("Bütün bilgiler kaydedildi ve sistem kapatıldı.");
                        company.writeToFile();
                        flag = false;
                        break;
                    case 1:
                        System.out.println("• Köpek eklemek için 1'e,\n• Kedi eklemek için 2'e,\n• Kuş eklemek için 3'e basınız: ");
                        answer = input.nextInt();
                        input.nextLine(); //int okumadaki enter'ı okuyor
                        switch (answer){
                            case 1:
                                dog = company.createDog();
                                company.addAnimal(dog);
                                break;
                            case 2:
                                cat = company.createCat();
                                company.addAnimal(cat);
                                break;
                            case 3:
                                bird = company.createBird();
                                company.addAnimal(bird);
                                break;
                            default:
                                System.out.println("Yanlış bir tuşlama yaptınız. Tekrar deneyin..");
                                break;
                        }
                        break;
                    case 2:
                        company.getDonation();
                        break;
                    case 3:
                        company.addSource();
                        break;
                    case 4:
                        company.makeOwner();
                        break;
                    case 5:
                        company.showDogs();
                        company.showCats();
                        company.showBirds();
                        System.out.println("Beslemek istediğiniz hayvanın ID'sini giriniz: ");
                        answer = input.nextInt();
                        input.nextLine(); //int okumadaki enter'ı okuyor
                        company.feedAnimal(company.findAnimal(String.format("%04d", answer)));
                        break;
                    case 6:
                        System.out.println("• Köpek silmek için 1'e,\n• Kedi silmek için 2'e,\n• Kuş silmek için 3'e basınız: ");
                        answer = input.nextInt();
                        input.nextLine(); //int okumadaki enter'ı okuyor
                        switch (answer){
                            case 1:
                                company.showDogs();
                                System.out.println("Silmek istediğiniz köpeğin ID'sini giriniz: ");
                                answer = input.nextInt();
                                input.nextLine();
                                company.removeDog(String.format("%04d", answer));
                                break;
                            case 2:
                                company.showCats();
                                System.out.println("Silmek istediğiniz kedinin ID'sini giriniz: ");
                                answer = input.nextInt();
                                input.nextLine();
                                company.removeCat(String.format("%04d", answer));
                                break;
                            case 3:
                                company.showBirds();
                                System.out.println("Silmek istediğiniz kuşun ID'sini giriniz: ");
                                answer = input.nextInt();
                                input.nextLine();
                                company.removeBird(String.format("%04d", answer));
                                break;
                            default:
                                System.out.println("Yanlış bir tuşlama yaptınız. Tekrar deneyin..");
                                break;
                        }
                        break;
                    case 7:
                        company.showDonations();
                        company.removeDonation();
                        break;
                    case 8:
                        company.showPersons();
                        company.removePerson();
                        break;
                    case 9:
                        company.showDogs();
                        company.showCats();
                        company.showBirds();
                        break;
                    case 10:
                        company.showAnimalsOwnedByPerson();
                        break;
                    case 11:
                        company.showPersons();
                        break;
                    case 12:
                        company.showDonations();
                        break;
                    case 13:
                        company.showUsernameAndPassword();
                        break;
                    case 14:
                        company.changeUsernameAndPassword();
                        break;
                }
            }catch (Exception e){
                System.out.println("Bir hata oluştu. Hata sebebi:\n" + e);
            }
        }while(flag == true);
    }
}
