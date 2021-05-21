public class BankAccount {
    private int id;
    private int money;

    public BankAccount(int id, int money) {
        this.id = id;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }
}
