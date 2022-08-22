package utils;

public class Polynom {
    // Начало списка
    private Monochlen head;
    private final double e = 1e-5;


    public Polynom(int deg, double coef) {
        head = new Monochlen(deg, coef);
    }

    public Polynom() {
        head = null;
    }

    public Polynom(Monochlen from) { //копия
        if (from != null) {
            //копируем голову
            head = new Monochlen(from);
            Monochlen current = head;
            from = from.getNext();
            //копируем остальные
            while (from != null) {
                current.setNext(new Monochlen(from));
                current = current.getNext();
                from = from.getNext();
            }
        } else {
            head = null;
        }
    }

    // Сложение полиномов
    public void add(Polynom secondPolynom) {
        // Текущая, следующая и предыдущая вершины
        Monochlen prevMon = null;
        Monochlen currentMon = head;
        Monochlen secondMon = secondPolynom.head;

        // Проверки на конец и нулевые коэффициенты
        if (currentMon == null) {
            head = new Polynom(secondMon).head;
            return;
        }

        if (secondMon == null) return;

        // Пока не дойдем до конца одного из полиномов
        while (currentMon != null && secondMon != null) {
            // Если текущая степень меньше вставляем в начало
            if (currentMon.getDeg() < secondMon.getDeg()) {
                if (prevMon == null) {
                    head = new Monochlen(secondMon);
                    prevMon = head;
                    head.setNext(currentMon);
                } else {
                    prevMon.setNext(new Monochlen(secondMon));
                    prevMon = prevMon.getNext();
                    prevMon.setNext(currentMon);
                }
                secondMon = secondMon.getNext();
            } else if (currentMon.getDeg() > secondMon.getDeg()) { // Если текущая степень больше - сдвигаемся
                prevMon = currentMon;
                currentMon = currentMon.getNext();
            } else { // Если равны - меняем коэффициенты
                double newCoefficient = currentMon.getChlenCoeff() + secondMon.getChlenCoeff();
                if (Math.abs(newCoefficient) > e) {
                    currentMon.setChlenCoeff(newCoefficient);
                    prevMon = currentMon;
                } else {
                    if (prevMon == null) {
                        head = head.getNext();
                    } else {
                        prevMon.setNext(currentMon.getNext());
                    }
                }

                currentMon = currentMon.getNext();
                secondMon = secondMon.getNext();
            }
        }

        // Слияние остатка
        if (secondMon != null) {
            prevMon.setNext(new Polynom(secondMon).head);
        }

    }

    // Умножение полинома на число
    public void mulByNum(double num) {
        if (isZero(num)) {
            head = null;
            return;
        }
        Monochlen tmp = head;
        while (tmp != null) {
            if (!isZero(tmp.getChlenCoeff())) {
                tmp.setChlenCoeff(tmp.getChlenCoeff() * num);
                tmp = tmp.getNext();
            }
        }
    }

    // Умножение полиномов
    public void multiply(Polynom secondPolynom) {
        Monochlen currentMon = head;
        Monochlen secondMon = secondPolynom.head;

        // Если хотя бы один полином пустой - обнуляем итоговый полином (сохраняем в первый)
        if (currentMon == null || secondMon == null) {
            head = null;
            return;
        }

        // Перебираем каждый элемент первого полинома, перемножаем со вторым и записываем во временный полином
        Polynom newPolynom = new Polynom();
        do {
            secondMon = secondPolynom.head;
            Monochlen newMon = newPolynom.head;

            do {
                int countDeg = currentMon.getDeg() + secondMon.getDeg();
                double countChlenCoeff = currentMon.getChlenCoeff() * secondMon.getChlenCoeff();
                // Если новый коэффициент равен нулю, то записывать эту вершину полинома не имеет смысла - идем дальше
                if (Math.abs(countChlenCoeff) <= e) {
                    secondMon = secondMon.getNext();
                } else {
                    // Если временный полином пустой, то записываем ему в голову новую вершину
                    if (newMon == null) {
                        newPolynom.head = new Monochlen(countDeg, countChlenCoeff);
                        newMon = newPolynom.head;
                        secondMon = secondMon.getNext();
                    } else if (countDeg == newMon.getDeg()) { // Если степень новой вершины равна текущему элементу временного полинома, то просто складываем коэффициенты
                        newMon.setChlenCoeff(newMon.getChlenCoeff() + countChlenCoeff);
                        secondMon = secondMon.getNext();
                    } else if (newMon.getNext() == null) { // Если дальше элементов нет, то записываем новую вершину полинома
                        newMon.setNext(new Monochlen(countDeg, countChlenCoeff));
                        newMon = newMon.getNext();
                        secondMon = secondMon.getNext();
                    } else if (countDeg < newMon.getDeg() && countDeg > newMon.getNext().getDeg()) { // Если во временном полиноме текщая и следующая за ней вершина по степени находятся меньше посчитанной степени, то записываем новую вершину между ними
                        Monochlen tmpMon = new Monochlen(countDeg, countChlenCoeff);
                        tmpMon.setNext(newMon.getNext());
                        newMon.setNext(tmpMon);
                        newMon = newMon.getNext();
                        secondMon = secondMon.getNext();
                    } else { // Если ни один вариант не подошел - движемся дальше по временному полиному
                        newMon = newMon.getNext();
                    }
                }
            } while (secondMon != null);
            currentMon = currentMon.getNext();
        } while (currentMon != null);

        // Обновляем состояние текущего полинома
        head = newPolynom.head;
    }

    // Подсчет значения полинома в точке
    public double calculateTheValue(double arg) {
        double result = 0;
        Monochlen cur = this.head;
        int total = cur.getDeg();

        for (int i = total; i > 0; i--) {
            if (cur != null && cur.getDeg() == i) {
                result += cur.getChlenCoeff();
                result *= arg;
                cur = cur.getNext();
            } else {
                result *= arg;
            }
        }
        if (cur != null && cur.getDeg() == 0) {
            result += cur.getChlenCoeff();
        }
        return result;
    }


    // Печать полинома
    public void print() {
        if (this.head == null) {
            return;
        }
        Monochlen cur = this.head;
        System.out.printf("%15.6E", cur.getChlenCoeff());
        System.out.print(" * x^" + cur.getDeg());
        cur = cur.getNext();
        while (cur != null) {
            if (isZero(cur.getChlenCoeff())) {
                cur = cur.getNext();
                continue;
            }
            if (cur.getChlenCoeff() > 0) {
                System.out.print(" +");
            }
            System.out.printf("%15.6E", cur.getChlenCoeff());
            if (cur.getDeg() != 0) {
                if (cur.getDeg() == 1) {
                    System.out.print(" * x");
                } else {
                    System.out.print(" * x^" + cur.getDeg());
                }
            }
            cur = cur.getNext();
        }
        System.out.println();
    }

    public Monochlen getHead() {
        return head;
    }

    public void setHead(Monochlen head) {
        this.head = head;
    }

    private boolean isZero(double x) {
        return Math.abs(x) < e;
    }

    private void setMonParameters(Monochlen current, Monochlen next, double coeff, int deg) {
        current.setChlenCoeff(coeff);
        current.setDeg(deg);
        current.setNext(next);
    }

    private void createMon(double coeff, int deg) {
        head.setChlenCoeff(coeff);
        head.setDeg(deg);
        head.setNext(null);
    }


}
