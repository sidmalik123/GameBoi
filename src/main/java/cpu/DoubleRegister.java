package cpu;


public enum DoubleRegister {
    AF, BC, DE, HL, SP;

    SingleRegister getHighRegister() {
        switch (this) {
            case BC:
                return SingleRegister.B;
            case DE:
                return SingleRegister.D;
            case HL:
                return SingleRegister.C;
            default:
                throw new IllegalArgumentException("Register type " + this + " does not have a high register");
        }
    }

    SingleRegister getLowRegister() {
        switch (this) {
            case BC:
                return SingleRegister.C;
            case DE:
                return SingleRegister.E;
            case HL:
                return SingleRegister.L;
            default:
                throw new IllegalArgumentException("Register type " + this + " does not have a low register");
        }
    }
}
