import abstracts.Creature;
import creatures.*;
import enums.*;
import exceptions.*;
import items.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<Creature> creatures = new ArrayList<>();

        Bendum bendum = Bendum.getInstance();
        creatures.add(bendum);
        System.out.println(bendum.notRespond() + ".");

        Doono doono = Doono.getInstance();
        creatures.add(doono);

        System.out.println(doono.lookOut() + Direction.IN + Place.HALL + ".");

        Twistum twistum = Twistum.getInstance();
        creatures.add(twistum);

        System.out.println(doono.hear(twistum) + ".");

        Shorty[] shorties = new Shorty[13];
        for (int i = 0; i < shorties.length; i++) {
            shorties[i] = new Shorty();
            creatures.add(shorties[i]);
        }

        if (creatures.size() < 4) {
            throw new NotEnoughCreaturesException("Ошибка: Слишком мало действующих лиц!");
        }

        for (Creature creature : creatures) {
            System.out.println(creature.takeAlarm() + ".");
            System.out.println(creature.rush() + Direction.TO + Place.EXIT + ".");
        }

        Order order = new Order();
        System.out.println(order.execute() + ".");

        Rope rope = new Rope();
        System.out.println(doono.tie(Direction.AROUND) + rope.first_end() + Direction.AROUND + doono.getWaist() + ".");

        DoorHandle doorHandle = new DoorHandle();
        System.out.println(doono.tie(Direction.TO) + rope.other_end() + Direction.TO + doorHandle + ".");

        doono.setStatus(Parameter.STRICT);
        System.out.println(doono.say() + ".");
        doono.setStatus(Parameter.DEFAULT);

        System.out.println(doono.tiltBody() + ".");

        Doorstep doorstep = new Doorstep();
        doono.setStatus(Parameter.FORCE);
        System.out.println(doono.pushOffWithLegs() + Direction.FROM + doorstep + ".");
        doono.setStatus(Parameter.DEFAULT);

        System.out.println(doono.fly() + Direction.INTHEDIRECTION + Place.WORKSHOP + ".");
        System.out.println(Place.WORKSHOP + Place.be() + Direction.NEAR + Place.HOME + ".");

        try {
            System.out.println(doono.Calculate());
        }
        catch (NotCalculateException e) {
            System.out.println("Ошибка: " + e.getMessage() + ".");
        }
        finally {
            System.out.println(doono.fly() + ".");
        }

        WeatherVane weatherVane = new WeatherVane();
        System.out.println(doono.flying() + Direction.OVER + Place.WORKSHOP + ", " + doono.grabWithHand() + Direction.WITH + weatherVane + ",");
        System.out.println(weatherVane + weatherVane.mean() + ".");

        System.out.println(doono.speedEffect(doono.getSpeed_change()) + ".");

        Downpipe downpipe = new Downpipe();
        System.out.println(doono.climbDown() + Direction.DUP + downpipe + ".");

        Door door = new Door();
        System.out.println(door.open(doono) + ".");

        System.out.println(doono.toString() + Direction.IN + Place.WORKSHOP + ".");

        for (Shorty shorty : shorties) {
            shorty.setStatus(Parameter.TENSION);
            System.out.println(shorty.watch(doono) + ".");
            shorty.setStatus(Parameter.DEFAULT);
        }

        System.out.println(doono.lookOut() + Direction.OF + Place.WORKSHOP + ".");

        System.out.println(doono.jump() + Direction.TO + Place.PAVILION + ".");
        System.out.println(doono.lookIn() + Direction.INSIDE + ".");

        System.out.println(bendum.notBe() + Direction.IN + Place.PAVILION + ".");

        for (Shorty shorty : shorties) {
            System.out.println(shorty.pull() + rope + ".");
        }
        System.out.println(doono.toString() + Direction.IN + Place.HOME + ".");

        doono.setStatus(Parameter.INSTANTLY);
        System.out.println(doono.climbUp() + Direction.DUP + downpipe + Direction.ON + Place.ROOF + ".");
        doono.setStatus(Parameter.DEFAULT);

        System.out.println(doono.wantLookAround() + ".");

        System.out.println(doono.lostControl());
        doono.setStatus(Parameter.NOTAFRAID);
        System.out.println(doono + ".");
        doono.setStatus(Parameter.DEFAULT);

        System.out.println(doono.know(shorties[(int)(Math.random() * (creatures.size()))].canPull() + Direction.ON + rope + " " + doono + Direction.BACK + "."));
    }
}