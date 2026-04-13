import java.util.*;

abstract class Member{
    protected String id;
    protected String nama;
    protected int saldo;

    public Member(String id, String nama){
        this.id = id;
        this.nama = nama;
        this.saldo = 0;
    }

    public void topUp(int jumlah){
        this.saldo += jumlah;
    }

    public int getSaldo(){
        return saldo;
    }

    public abstract String getInfo();

    public abstract int hitungPembayaran(int hargaDasar, int sesi);

    public int buy(String layanan, int sesi){
        int hargaLayanan;

        switch (layanan){
            case "cardio": hargaLayanan = 20000; break;
            case "yoga": hargaLayanan = 25000; break;
            case "personal_training": hargaLayanan = 40000; break;
            default: return -1;
        }

        int hargaDasar = hargaLayanan * sesi;
        return hitungPembayaran(hargaDasar, sesi);
    }
}

class Reguler extends Member{
    public Reguler(String id, String nama){
        super(id, nama);
    }

    @Override
    public String getInfo(){
        return id + " | " + nama + " | Reguler | Saldo: " + saldo;
    }

    @Override
    public int hitungPembayaran(int hargaDasar, int sesi){
        int subtotal = hargaDasar;

        if (sesi > 5 ) {
            int diskonSesi = hargaDasar * 10 / 100;
            subtotal -= diskonSesi;
        }

        int pajak = subtotal * 5 / 100;
        int total = subtotal + pajak;

        return Math.max(total, 0);
    }
}

class VIP extends Member{
    public VIP (String id, String nama){
        super(id, nama);
    }

    @Override 
    public String getInfo(){
        return id + " | " + nama + " | VIP | saldo: " + saldo;
    }

    @Override
    public int hitungPembayaran(int hargaDasar, int sesi){
        int subtotal = hargaDasar;

        if (sesi > 5) {
            int diskonSesi = hargaDasar * 10 /100;
            subtotal -= diskonSesi;
        }

        int diskonVIP = hargaDasar * 15 /100;
        subtotal -= diskonVIP;

        int pajak = subtotal * 5 /100;
        int total = subtotal + pajak;
        
        return Math.max(total, 0);
    }
}

class GymSystem{
    private List<Member> members;

    public GymSystem(){
        members = new ArrayList<>();
    }

    public Member findMember(String id){
        for (Member m : members){
            if (m.id.equals(id)) return m;
        }
        return null;
    }

    public void addMember(String tipe, String id, String nama){
        if (findMember(id) != null) {
            System.out.println("Member sudah terdaftar");
            return;
        }

        Member member;
        if (tipe.equalsIgnoreCase("REGULER")) {
            member = new Reguler(id, nama);
            System.out.println("Reguler " + id + " berhasil ditambahkan");
        } else if (tipe.equalsIgnoreCase("VIP")) {
            member = new VIP(id, nama);
            System.out.println("VIP " + id + " berhasil ditambahkan");
        }else {
            System.out.println("Tipe member tidak valid");
            return;
        }

        members.add(member);
    }

    public void topUp(String id, int jumlah){
        Member member = findMember(id);
        if (member == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }
        member.topUp(jumlah);
        System.out.println("Saldo " + id + ": " + member.getSaldo());
    }

    public void buy(String id, String layanan, int sesi){
        Member member = findMember(id);
        if (member == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }

        if (!layanan.equals("cardio") && !layanan.equals("yoga") && !layanan.equals("personal_training")) {
            System.out.println("Layanan tidak valid");
            return;
        }

        int total = member.buy(layanan, sesi);

        if (member.getSaldo() < total) {
            System.out.println("Saldo " + id + " tidak cukup");
            return;
        }

        member.saldo -= total;
        System.out.println("Total bayar " + id + ": " + total);
        System.out.println("Saldo " + id + ": " + member.getSaldo());
    }

    public void check(String id){
        Member member = findMember(id);
        if (member == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }
        System.out.println(member.getInfo());
    }

    public int count(){
        return members.size();
    }
}

public class MembershipGym{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GymSystem gym = new GymSystem();

        int n = Integer.parseInt(sc.nextLine().trim());
        
        for (int i = 0; i < n; i++){
            String line = sc.nextLine().trim();
            String[] parts = line.split(" ");
            String cmd = parts[0].toUpperCase();

            switch (cmd) {
                case "ADD":
                    gym.addMember(parts[1], parts[2], parts[3]);
                    break;

                case "TOPUP":
                    gym.topUp(parts[1], Integer.parseInt(parts[2]));
                    break;

                case "BUY":
                    gym.buy(parts[1], parts[2], Integer.parseInt(parts[3]));
                    break;
                
                case "CHECK":
                    gym.check(parts[1]);
                    break;

                case "COUNT":
                    System.out.println("Total member: " + gym.count());
                    break;
                
                default:
                    System.out.println("Perintah tidak dikenal");
                    break;
            }
        }
        sc.close();
    }
}