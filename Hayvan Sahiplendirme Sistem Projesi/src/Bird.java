public class Bird extends Animal{
    public Bird(int age, String kind, String healthStatus, String foundDate, int foodItAte){
        super(age, kind, healthStatus, foundDate, foodItAte);
    }

    public String toString(){
        Person owner;
        String intro = "";
        intro += "ID :" + getID() + " | Yaşı: " + getAge() + " | Türü: " + getKind() + " | Sağlık Durumu: " + getHealthStatus() + " | Bulunma Tarihi: " +
                getFoundDate() + " | Yediği Günlük Yem: " + getFoodItAte() + " gr";
        if((owner = getOwner()) != null){
            intro += " | Sahibi: " + owner.getName() + " " + owner.getSurname() + " | Sahiplenilme Tarihi: " + getOwnershipDate();
        }
        return intro;
    }
}