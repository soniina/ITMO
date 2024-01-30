package creatures;
import abstracts.*;
import enums.*;
import exceptions.NotCalculateException;
import interfaces.*;

public class Doono extends Creature implements Flyable {
    private static Doono doono;
    private Doono() {
        setName("Знайка");
    }

    public static Doono getInstance() {
        if (doono == null) {
            doono = new Doono();
        }
        return doono;
    }

    static int y = 0;
    static int speedChange = 0;

    static public class Effect {
        String description = "";
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
    Effect effect = new Effect();

    public class BodyPart {
        private String name;
        private String useByName;
        BodyPart(String bodyPart) {
            name = bodyPart;
        }
        BodyPart(String bodyPart, String useByName) {
            name = bodyPart;
            this.useByName = useByName;
        }
        public String getName() {
            return name;
        }
        public String getUseByName() {
            return useByName;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setUseByName(String useByName) {
            this.useByName = useByName;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public String grabWithHand() {
        speedChange = Math.min(speedChange, 0) - 1;
        speedEffect(-1);
        BodyPart hand = new BodyPart("Рука", "рукой");
        return toString() + " ухватился " + hand.getUseByName();
    }

    public String tiltBody() {
        BodyPart body = new BodyPart("Тело");
        return toString() + " наклонил " + body;
    }

    public String getWaist() {
        BodyPart waist = new BodyPart("Пояс");
        return waist.toString();
    }
    public String pushOffWithLegs() {
        y += 1;
        speedChange = Math.max(speedChange, 0) + 1;
        effect.setDescription("Толчок");
        BodyPart legs = new BodyPart("Нога", "ногами");
        return toString() + " оттолкнулся " + legs.getUseByName();
    }

    public String Calculate() throws NotCalculateException {
        y += 1;
        speedChange = Math.max(speedChange, 0) + 1;
        if (y > 1) {
            throw new NotCalculateException(toString() + switch (y) {
                case(2) -> Parameter.ALITTLE + " не рассчитал " + effect;
                case(3) -> " не рассчитал " + effect;
                default -> "сильно ошибся в расчётах " + effect;
            });
        }
        return toString() + "верно рассчитал " + effect;
    }
    public String speedEffect(int change) {
        if (change > 0) return "Это ускорило " + effect;
        else if (change < 0) return "Это задержало " + effect;
        else return "";
    }
    public int getSpeed_change() {
        return speedChange;
    }
    public String climbDown() {
        y -= 1;
        return toString() + " спустился";
    }
    public String climbUp() {
        y += 1;
        return toString() + " вскарабкался";
    }
    public String lookOut() {
        return toString() + " выглянул";
    }
    public String lookIn()  {
        return toString() + " заглянул";
    }

    public String jump() {
        y += 1;
        y -= 1;
        return toString() + " прыгнул";
    }

    public String wantLookAround() {
        return toString() + " хотел оглянуться по сторонам";
    }

    public String hear(Creature creature) {
        return toString() + " услыхал слова " + creature;
    }

    public String know(String knowledge) {
        return toString() + " знал, что " + knowledge;
    }
    @Override
    public String fly() {
        return toString() + switch (y) {
            case 0 -> "";
            case 1 -> " полетел";
            case 2 -> " поднялся выше, чем было надо";
            default -> "лететь";
        };
    }
    @Override
    public String flying() {
        if (y > 0) {
            effect.setDescription("полёт");
            return toString() + " пролетал";
        }
        return null;
    }

    Tieable tieable = new Tieable() {
        @Override
        public String tie(Direction direction) {
            return doono.toString() + switch (direction) {
                case AROUND -> " обвязал ";
                case TO -> " привязал ";
                default -> "вязать";
            };
        }
    };

    public String tie(Direction direction) {
        return tieable.tie(direction);
    }

    public String lostControl() {
        class GustOfWind {
            private String name = "Порыв ветра";
            private String status;

            @Override
            public String toString() {
                return name;
            }

            public String blow() {
                return toString() + " налетел";
            }
            public String blowOff(Creature creature, Place place) {
                return toString() + " сдул c " + place  + " " + creature;
            }
            public String carryAway(Creature creature) {
                return toString() + " понёс в сторону " + creature;
            }
        }

        GustOfWind gustOfWind = new GustOfWind();
        gustOfWind.status = "Неожиданно ";
        return gustOfWind.status + gustOfWind.blow() + ".\n" + gustOfWind.blowOff(doono, Place.ROOF) + ".\n" + gustOfWind.carryAway(doono) + ".";
    }
}