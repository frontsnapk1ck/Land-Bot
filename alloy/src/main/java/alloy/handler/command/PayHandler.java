package alloy.handler.command;

import alloy.gameobjects.player.Player;

public class PayHandler {

    public static boolean canPay(Player from, int amount) 
    {
		return from.getBal() >= amount;
	}

    public static void pay(Player to, Player from, int amount) 
    {
        from.spend(amount);
        to.addBal(amount);
	}
    
}
