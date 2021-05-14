package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void agregarMovimiento(Movimiento movimiento) { //CODE SMELL: Long parameter.
   /* Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);*/
      movimientos.add(movimiento);
      this.setSaldo(this.getSaldo()+movimiento.getMonto());
    }
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }


  public boolean chequearSiElMontoIngresadoEsValido(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

   boolean chequearSiPuedeHacerElDeposito() {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

     boolean chequearSiSePuedeExtraerEseMonto ( double cuanto){
      if (cuanto <= 0) {
        throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
      }
      if (getSaldo() - cuanto < 0) {
        throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
      }
    }

    public boolean chequearSiLlegoAlLimiteDeHoy(double cuanto){
      double montoExtraidoHoy = this.getMontoExtraidoA(LocalDate.now());
      double limite = 1000 - montoExtraidoHoy;
      if (cuanto > limite) {
        throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
            + " diarios, límite: " + limite);
      }

      public void poner ( double cuanto)
      { // CODE SMELL: Long method. Este método hace demasiadas cosas, podrían delegarse sus tareas en submétodos.
    /*if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);*/

        this.chequearSiElMontoIngresadoEsValido(cuanto);
        this.chequearSiPuedeHacerElDeposito();
        new Deposito(LocalDate.now(), cuanto).agregateA(this); //CODE SMELLS: Type tests.
      }

      public void sacar(double cuanto){ //CODE SMELL: Long method, igual que el metodo poner.
    /*if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);*/

        this.chequearSiSePuedeExtraerEseMonto(cuanto);
        this.chequearSiLlegoAlLimiteDeHoy(cuanto);
        new Extraccion(LocalDate.now(), cuanto).agregateA(this); //CODE SMELLS: Type tests.

      }


    }
  }
}
