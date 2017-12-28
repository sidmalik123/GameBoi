package cpu.instructions;

import com.google.inject.Inject;
import core.BitUtils;
import cpu.CPU;
import cpu.UnknownInstructionException;
import cpu.alu.ALU;
import cpu.clock.Clock;
import cpu.registers.Flag;
import cpu.registers.Register;
import cpu.registers.Registers;
import mmu.MMU;

/**
 * Concrete class for InstructionExecutor
 * */
public class InstructionExecutorImpl implements InstructionExecutor {

    private MMU mmu;
    private Registers registers;
    private Clock clock;
    private ALU alu;
    private CPU cpu;

    @Inject
    public InstructionExecutorImpl(MMU mmu, Registers registers, Clock clock, ALU alu, CPU cpu) {
        this.mmu = mmu;
        this.registers = registers;
        this.clock = clock;
        this.alu = alu;
        this.cpu = cpu;
    }

    @Override
    public void executeInstruction() {
        int instruction = getImmediateByte();

        switch (instruction) {
            case 0x00: break;
            case 0x01: loadRegisterWithImmediateWord(Register.BC); break;
            case 0x02: mmu.write(registers.read(Register.BC), registers.read(Register.A)); break;
            case 0x03: incWordRegister(Register.BC); break;
            case 0x04: incByteRegister(Register.B); break;
            case 0x05: decByteRegister(Register.B); break;
            case 0x06: registers.write(Register.B, getImmediateByte()); break;
            case 0x07: rotateByteRegisterLeft(Register.A, false); break;
            case 0x08: loadWordIntoMemory(getImmediateWord(), registers.read(Register.SP)); break;
            case 0x09: addWords(Register.HL, Register.BC); break;
            case 0x0A: registers.write(Register.A, mmu.read(registers.read(Register.BC))); break;
            case 0x0B: decWordRegister(Register.BC); break;
            case 0x0C: incByteRegister(Register.C); break;
            case 0x0D: decByteRegister(Register.D); break;
            case 0x0E: registers.write(Register.C, getImmediateByte()); break;
            case 0x0F: rotateByteRegisterRight(Register.A, false); break;
            case 0x10: cpu.stop(); break;
            case 0x11: loadRegisterWithImmediateWord(Register.DE); break;
            case 0x12: mmu.write(registers.read(Register.DE), registers.read(Register.A)); break;
            case 0x13: incWordRegister(Register.DE); break;
            case 0x14: incByteRegister(Register.D); break;
            case 0x15: decByteRegister(Register.D); break;
            case 0x16: loadRegisterWithImmediateByte(Register.D); break;
            case 0x17: rotateByteRegisterLeft(Register.A, true); break;
            case 0x18: jr(true); break;
            case 0x19: addWords(Register.HL, Register.DE); break;
            case 0x1A: registers.write(Register.A, mmu.read(registers.read(Register.DE))); break;
            case 0x1B: decWordRegister(Register.DE); break;
            case 0x1C: incByteRegister(Register.E); break;
            case 0x1D: decByteRegister(Register.E); break;
            case 0x1E: loadRegisterWithImmediateByte(Register.E); break;
            case 0x1F: rotateByteRegisterRight(Register.A, true); break;
            case 0x20: jr(!registers.getFlag(Flag.ZERO)); break; // NZ
            case 0x21: loadRegisterWithImmediateWord(Register.DE); break;
            case 0x22: mmu.write(registers.read(Register.HL), registers.read(Register.A)); incWordRegister(Register.HL); break;
            case 0x23: incWordRegister(Register.HL); break;
            case 0x24: incByteRegister(Register.H); break;
            case 0x25: decByteRegister(Register.H); break;
            case 0x26: loadRegisterWithImmediateByte(Register.H); break;
            case 0x27: break; // TODO - DAA
            case 0x28: jr(registers.getFlag(Flag.ZERO)); break; // JR Z
            case 0x29: addWords(Register.HL, Register.HL); break;
            case 0x2A: registers.write(Register.A, mmu.read(registers.read(Register.HL))); incWordRegister(Register.HL); break;
            case 0x2B: decWordRegister(Register.HL); break;
            case 0x2C: incByteRegister(Register.L); break;
            case 0x2D: decByteRegister(Register.L); break;
            case 0x2E: loadRegisterWithImmediateByte(Register.E); break;
            case 0x2F: registers.write(Register.A, alu.complementByte(registers.read(Register.A))); break;
            case 0x30: jr(!registers.getFlag(Flag.CARRY)); break;
            case 0x31: loadRegisterWithImmediateWord(Register.SP); break;
            case 0x32: mmu.write(registers.read(Register.HL), registers.read(Register.A)); decWordRegister(Register.HL); break;
            case 0x33: incWordRegister(Register.SP); break;
            case 0x34: mmu.write(registers.read(Register.HL), alu.incByte(mmu.read(registers.read(Register.HL)))); break;
            case 0x35: mmu.write(registers.read(Register.HL), alu.decByte(mmu.read(registers.read(Register.HL)))); break;
            case 0x36: mmu.write(registers.read(Register.HL), getImmediateByte()); break;
            case 0x37: SCF(); break;
            case 0x38: jr(registers.getFlag(Flag.CARRY)); break; // JR C
            case 0x39: addWords(Register.HL, Register.SP); break;
            case 0x3A: registers.write(Register.A, mmu.read(registers.read(Register.HL))); decWordRegister(Register.HL); break;
            case 0x3B: decWordRegister(Register.SP); break;
            case 0x3C: incByteRegister(Register.A); break;
            case 0x3D: decByteRegister(Register.A); break;
            case 0x3E: loadRegisterWithImmediateByte(Register.A); break;
            case 0x3F: CCF(); break;
            case 0x40: loadRegisterFromRegister(Register.B, Register.B); break;
            case 0x41: loadRegisterFromRegister(Register.B, Register.C); break;
            case 0x42: loadRegisterFromRegister(Register.B, Register.D); break;
            case 0x43: loadRegisterFromRegister(Register.B, Register.E); break;
            case 0x44: loadRegisterFromRegister(Register.B, Register.H); break;
            case 0x45: loadRegisterFromRegister(Register.B, Register.L); break;
            case 0x46: registers.write(Register.B, mmu.read(registers.read(Register.HL))); break;
            case 0x47: loadRegisterFromRegister(Register.B, Register.A); break;
            case 0x48: loadRegisterFromRegister(Register.C, Register.B); break;
            case 0x49: loadRegisterFromRegister(Register.C, Register.C); break;
            case 0x4A: loadRegisterFromRegister(Register.C, Register.D); break;
            case 0x4B: loadRegisterFromRegister(Register.C, Register.E); break;
            case 0x4C: loadRegisterFromRegister(Register.C, Register.H); break;
            case 0x4D: loadRegisterFromRegister(Register.C, Register.L); break;
            case 0x4E: registers.write(Register.C, mmu.read(registers.read(Register.HL))); break;
            case 0x4F: loadRegisterFromRegister(Register.C, Register.A); break;
            case 0x50: loadRegisterFromRegister(Register.D, Register.B); break;
            case 0x51: loadRegisterFromRegister(Register.D, Register.C); break;
            case 0x52: loadRegisterFromRegister(Register.D, Register.D); break;
            case 0x53: loadRegisterFromRegister(Register.D, Register.E); break;
            case 0x54: loadRegisterFromRegister(Register.D, Register.H); break;
            case 0x55: loadRegisterFromRegister(Register.D, Register.L); break;
            case 0x56: registers.write(Register.D, mmu.read(registers.read(Register.HL))); break;
            case 0x57: loadRegisterFromRegister(Register.D, Register.A); break;
            case 0x58: loadRegisterFromRegister(Register.E, Register.B); break;
            case 0x59: loadRegisterFromRegister(Register.E, Register.C); break;
            case 0x5A: loadRegisterFromRegister(Register.E, Register.D); break;
            case 0x5B: loadRegisterFromRegister(Register.E, Register.E); break;
            case 0x5C: loadRegisterFromRegister(Register.E, Register.H); break;
            case 0x5D: loadRegisterFromRegister(Register.E, Register.L); break;
            case 0x5E: registers.write(Register.E, mmu.read(registers.read(Register.HL))); break;
            case 0x5F: loadRegisterFromRegister(Register.E, Register.A); break;
            case 0x60: loadRegisterFromRegister(Register.H, Register.B); break;
            case 0x61: loadRegisterFromRegister(Register.H, Register.C); break;
            case 0x62: loadRegisterFromRegister(Register.H, Register.D); break;
            case 0x63: loadRegisterFromRegister(Register.H, Register.E); break;
            case 0x64: loadRegisterFromRegister(Register.H, Register.H); break;
            case 0x65: loadRegisterFromRegister(Register.H, Register.L); break;
            case 0x66: registers.write(Register.H, mmu.read(registers.read(Register.HL))); break;
            case 0x67: loadRegisterFromRegister(Register.H, Register.A); break;
            case 0x68: loadRegisterFromRegister(Register.L, Register.B); break;
            case 0x69: loadRegisterFromRegister(Register.L, Register.C); break;
            case 0x6A: loadRegisterFromRegister(Register.L, Register.D); break;
            case 0x6B: loadRegisterFromRegister(Register.L, Register.E); break;
            case 0x6C: loadRegisterFromRegister(Register.L, Register.H); break;
            case 0x6D: loadRegisterFromRegister(Register.L, Register.L); break;
            case 0x6E: registers.write(Register.L, mmu.read(registers.read(Register.HL))); break;
            case 0x6F: loadRegisterFromRegister(Register.L, Register.A); break;
            case 0x70: loadHLMemoryFromRegister(Register.B); break;
            case 0x71: loadHLMemoryFromRegister(Register.C); break;
            case 0x72: loadHLMemoryFromRegister(Register.D); break;
            case 0x73: loadHLMemoryFromRegister(Register.E); break;
            case 0x74: loadHLMemoryFromRegister(Register.H); break;
            case 0x75: loadHLMemoryFromRegister(Register.L); break;
            case 0x76: cpu.halt(); break;
            case 0x77: loadHLMemoryFromRegister(Register.A); break;
            case 0x78: loadRegisterFromRegister(Register.A, Register.B); break;
            case 0x79: loadRegisterFromRegister(Register.A, Register.C); break;
            case 0x7A: loadRegisterFromRegister(Register.A, Register.D); break;
            case 0x7B: loadRegisterFromRegister(Register.A, Register.E); break;
            case 0x7C: loadRegisterFromRegister(Register.A, Register.H); break;
            case 0x7D: loadRegisterFromRegister(Register.A, Register.L); break;
            case 0x7E: loadRegisterFromHLMemory(Register.A); break;
            case 0x7F: loadRegisterFromRegister(Register.A, Register.A); break;
            case 0x80: addA(registers.read(Register.B), false); break;
            case 0x81: addA(registers.read(Register.C), false); break;
            case 0x82: addA(registers.read(Register.D), false); break;
            case 0x83: addA(registers.read(Register.E), false); break;
            case 0x84: addA(registers.read(Register.H), false); break;
            case 0x85: addA(registers.read(Register.L), false); break;
            case 0x86: addA(mmu.read(registers.read(Register.HL)), false);
            case 0x87: addA(registers.read(Register.A), false); break;
            case 0x88: addA(registers.read(Register.B), true); break;
            case 0x89: addA(registers.read(Register.C), true); break;
            case 0x8A: addA(registers.read(Register.D), true); break;
            case 0x8B: addA(registers.read(Register.E), true); break;
            case 0x8C: addA(registers.read(Register.H), true); break;
            case 0x8D: addA(registers.read(Register.L), true); break;
            case 0x8E: addA(mmu.read(registers.read(Register.HL)), true); break;
            case 0x8F: addA(registers.read(Register.A), true); break;
            case 0x90: subA(registers.read(Register.B), false); break;
            case 0x91: subA(registers.read(Register.C), false); break;
            case 0x92: subA(registers.read(Register.D), false); break;
            case 0x93: subA(registers.read(Register.E), false); break;
            case 0x94: subA(registers.read(Register.H), false); break;
            case 0x95: subA(registers.read(Register.L), false); break;
            case 0x96: subA(mmu.read(registers.read(Register.HL)), false); break;
            case 0x97: subA(registers.read(Register.A), false); break;
            case 0x98: subA(registers.read(Register.B), true); break;
            case 0x99: subA(registers.read(Register.C), true); break;
            case 0x9A: subA(registers.read(Register.D), true); break;
            case 0x9B: subA(registers.read(Register.E), true); break;
            case 0x9C: subA(registers.read(Register.H), true); break;
            case 0x9D: subA(registers.read(Register.L), true); break;
            case 0x9E: subA(mmu.read(registers.read(Register.HL)), true); break;
            case 0x9F: subA(registers.read(Register.A), true); break;
            case 0xA0: andA(registers.read(Register.B)); break;
            case 0xA1: andA(registers.read(Register.C)); break;
            case 0xA2: andA(registers.read(Register.D)); break;
            case 0xA3: andA(registers.read(Register.E)); break;
            case 0xA4: andA(registers.read(Register.H)); break;
            case 0xA5: andA(registers.read(Register.L)); break;
            case 0xA6: andA(mmu.read(registers.read(Register.HL))); break;
            case 0xA7: andA(registers.read(Register.A)); break;
            case 0xA8: xorA(registers.read(Register.B)); break;
            case 0xA9: xorA(registers.read(Register.C)); break;
            case 0xAA: xorA(registers.read(Register.D)); break;
            case 0xAB: xorA(registers.read(Register.E)); break;
            case 0xAC: xorA(registers.read(Register.H)); break;
            case 0xAD: xorA(registers.read(Register.L)); break;
            case 0xAE: xorA(mmu.read(registers.read(Register.HL))); break;
            case 0xAF: xorA(registers.read(Register.A)); break;
            case 0xB0: orA(registers.read(Register.B)); break;
            case 0xB1: orA(registers.read(Register.C)); break;
            case 0xB2: orA(registers.read(Register.D)); break;
            case 0xB3: orA(registers.read(Register.E)); break;
            case 0xB4: orA(registers.read(Register.H)); break;
            case 0xB5: orA(registers.read(Register.L)); break;
            case 0xB6: orA(mmu.read(registers.read(Register.HL))); break;
            case 0xB7: orA(registers.read(Register.A)); break;
            case 0xB8: cpA(registers.read(Register.B)); break;
            case 0xB9: cpA(registers.read(Register.C)); break;
            case 0xBA: cpA(registers.read(Register.D)); break;
            case 0xBB: cpA(registers.read(Register.E)); break;
            case 0xBC: cpA(registers.read(Register.H)); break;
            case 0xBD: cpA(registers.read(Register.L)); break;
            case 0xBE: cpA(mmu.read(registers.read(Register.HL))); break;
            case 0xBF: cpA(registers.read(Register.A)); break;
            case 0xC0: if (!registers.getFlag(Flag.ZERO)) ret(); break;
            case 0xC1: registers.write(Register.BC, popWordFromStack()); break;
            case 0xC2: if (!registers.getFlag(Flag.ZERO)) jp(); break;
            case 0xC3: jp(); break;
            case 0xC4: call(!registers.getFlag(Flag.ZERO)); break;
            case 0xC5: pushWordToStack(registers.read(Register.BC)); break;
            case 0xC6: addA(getImmediateByte(), false); break;
            case 0xC7: rst(0x00); break;
            case 0xC8: if (registers.getFlag(Flag.ZERO)) ret(); break;
            case 0xC9: ret(); break;
            case 0xCA: if (registers.getFlag(Flag.ZERO)) jp(); break;
            case 0xCB: executeExtendedOpcode(getImmediateByte()); break;
            case 0xCC: call(registers.getFlag(Flag.ZERO)); break;
            case 0xCD: call(true); break;
            case 0xCE: addA(getImmediateByte(), true); break;
            case 0xCF: rst(0x08); break;
            case 0xD0: if (!registers.getFlag(Flag.CARRY)) ret(); break;
            case 0xD1: registers.write(Register.DE, popWordFromStack()); break;
            case 0xD2: if (!registers.getFlag(Flag.CARRY)) jp(); break;
            case 0xD4: call(!registers.getFlag(Flag.CARRY)); break;
            case 0xD5: pushWordToStack(registers.read(Register.DE)); break;
            case 0xD6: subA(getImmediateByte(), false); break;
            case 0xD7: rst(0x10); break;
            case 0xD8: if (registers.getFlag(Flag.CARRY)) ret(); break;
            case 0xD9: registers.write(Register.PC, popWordFromStack()); break; // RETI - TODO enable interrupts
            case 0xDA: if (registers.getFlag(Flag.CARRY)) jp(); break;
            case 0xDC: call(registers.getFlag(Flag.CARRY)); break;
            case 0xDE: subA(getImmediateByte(), true); break;
            case 0xDF: rst(0x18); break;
            case 0xE0: mmu.write(0xFF00 + getImmediateByte(), registers.read(Register.A)); break;
            case 0xE1: registers.write(Register.HL, popWordFromStack()); break;
            case 0xE2: mmu.write(0xFF00 + registers.read(Register.C), registers.read(Register.A)); break;
            case 0xE5: pushWordToStack(registers.read(Register.HL)); break;
            case 0xE6: andA(getImmediateByte()); break;
            case 0xE7: rst(0x20); break;
            case 0xE8: registers.write(Register.SP, alu.addSignedByteToWord(registers.read(Register.SP), getImmediateByte())); break;
            case 0xE9: registers.write(Register.PC, registers.read(Register.HL)); break;
            case 0xEA: mmu.write(getImmediateWord(), registers.read(Register.A)); break;
            case 0xEE: xorA(getImmediateByte()); break;
            case 0xEF: rst(0x28); break;
            case 0xF0: registers.write(Register.A, mmu.read(0xFF00 + getImmediateByte())); break;
            case 0xF1: registers.write(Register.AF, popWordFromStack()); break;
            case 0xF2: registers.write(Register.A, mmu.read(0xFF00 + registers.read(Register.C))); break;
            case 0xF3: break; // Todo - Disable interrupts
            case 0xF5: pushWordToStack(registers.read(Register.AF)); break;
            case 0xF6: orA(getImmediateByte()); break;
            case 0xF7: rst(0x30); break;
            case 0xF8: registers.write(Register.HL, alu.addSignedByteToWord(registers.read(Register.SP), getImmediateByte())); break;
            case 0xF9: registers.write(Register.SP, registers.read(Register.HL)); break;
            case 0xFA: registers.write(Register.A, mmu.read(getImmediateWord())); break;
            case 0xFB: break; // Todo - enable interrupts
            case 0xFE: cpA(getImmediateByte()); break;
            case 0xFF: rst(0x38); break;

            default: throw new UnknownInstructionException("Invalid instruction 0x" + Integer.toHexString(instruction));
         }
    }

    private void executeExtendedOpcode(int instruction) {
        switch (instruction) {
            case 0x00: rotateByteRegisterLeft(Register.B, false); break;
            case 0x01: rotateByteRegisterLeft(Register.C, false); break;
            case 0x02: rotateByteRegisterLeft(Register.D, false); break;
            case 0x03: rotateByteRegisterLeft(Register.E, false); break;
            case 0x04: rotateByteRegisterLeft(Register.H, false); break;
            case 0x05: rotateByteRegisterLeft(Register.L, false); break;
            case 0x06: mmu.write(registers.read(Register.HL), alu.rotateByteLeft(mmu.read(registers.read(Register.HL)), false)); break;
            case 0x07: rotateByteRegisterLeft(Register.A, false); break;
            case 0x08: rotateByteRegisterRight(Register.B, false); break;
            case 0x09: rotateByteRegisterRight(Register.C, false); break;
            case 0x0A: rotateByteRegisterRight(Register.D, false); break;
            case 0x0B: rotateByteRegisterRight(Register.E, false); break;
            case 0x0C: rotateByteRegisterRight(Register.H, false); break;
            case 0x0D: rotateByteRegisterRight(Register.L, false); break;
            case 0x0E: mmu.write(registers.read(Register.HL), alu.rotateByteRight(mmu.read(registers.read(Register.HL)), false)); break;
            case 0x0F: rotateByteRegisterRight(Register.A, false); break;
            case 0x10: rotateByteRegisterLeft(Register.B, true); break;
            case 0x11: rotateByteRegisterLeft(Register.C, true); break;
            case 0x12: rotateByteRegisterLeft(Register.D, true); break;
            case 0x13: rotateByteRegisterLeft(Register.E, true); break;
            case 0x14: rotateByteRegisterLeft(Register.H, true); break;
            case 0x15: rotateByteRegisterLeft(Register.L, true); break;
            case 0x16: mmu.write(registers.read(Register.HL), alu.rotateByteLeft(mmu.read(registers.read(Register.HL)), true)); break;
            case 0x17: rotateByteRegisterLeft(Register.A, true); break;
            case 0x18: rotateByteRegisterRight(Register.B, true); break;
            case 0x19: rotateByteRegisterRight(Register.C, true); break;
            case 0x1A: rotateByteRegisterRight(Register.D, true); break;
            case 0x1B: rotateByteRegisterRight(Register.E, true); break;
            case 0x1C: rotateByteRegisterRight(Register.H, true); break;
            case 0x1D: rotateByteRegisterRight(Register.L, true); break;
            case 0x1E: mmu.write(registers.read(Register.HL), alu.rotateByteRight(mmu.read(registers.read(Register.HL)), true)); break;
            case 0x20: shiftByteRegisterLeft(Register.B); break;
            case 0x21: shiftByteRegisterLeft(Register.C); break;
            case 0x22: shiftByteRegisterLeft(Register.D); break;
            case 0x23: shiftByteRegisterLeft(Register.E); break;
            case 0x24: shiftByteRegisterLeft(Register.H); break;
            case 0x25: shiftByteRegisterLeft(Register.L); break;
            case 0x26: mmu.write(registers.read(Register.HL), alu.shiftByteLeft(mmu.read(registers.read(Register.HL)))); break;
            case 0x27: shiftByteRegisterLeft(Register.A); break;
            case 0x28: shiftByteRegisterRight(Register.B, false); break;
            case 0x29: shiftByteRegisterRight(Register.C, false); break;
            case 0x2A: shiftByteRegisterRight(Register.D, false); break;
            case 0x2B: shiftByteRegisterRight(Register.E, false); break;
            case 0x2C: shiftByteRegisterRight(Register.H, false); break;
            case 0x2D: shiftByteRegisterRight(Register.L, false); break;
            case 0x2E: mmu.write(registers.read(Register.HL), alu.shiftByteRight(mmu.read(registers.read(Register.HL)), false)); break;
            case 0x2F: shiftByteRegisterRight(Register.A, false); break;
            case 0x30: swapNibbles(Register.B); break;
            case 0x31: swapNibbles(Register.C); break;
            case 0x32: swapNibbles(Register.D); break;
            case 0x33: swapNibbles(Register.E); break;
            case 0x34: swapNibbles(Register.H); break;
            case 0x35: swapNibbles(Register.L); break;
            case 0x36: mmu.write(registers.read(Register.HL), alu.swapNibbles(mmu.read(registers.read(Register.HL)))); break;
            case 0x37: swapNibbles(Register.A); break;
            case 0x38: shiftByteRegisterRight(Register.B, true); break;
            case 0x39: shiftByteRegisterRight(Register.C, true); break;
            case 0x3A: shiftByteRegisterRight(Register.D, true); break;
            case 0x3B: shiftByteRegisterRight(Register.E, true); break;
            case 0x3C: shiftByteRegisterRight(Register.H, true); break;
            case 0x3D: shiftByteRegisterRight(Register.L, true); break;
            case 0x3E: mmu.write(registers.read(Register.HL), alu.shiftByteRight(mmu.read(registers.read(Register.HL)), true)); break;
            case 0x3F: shiftByteRegisterRight(Register.A, true); break;
            case 0x40: testBit(Register.B, 0); break;
            case 0x41: testBit(Register.C, 0); break;
            case 0x42: testBit(Register.D, 0); break;
            case 0x43: testBit(Register.E, 0); break;
            case 0x44: testBit(Register.H, 0); break;
            case 0x45: testBit(Register.L, 0); break;
            case 0x46: alu.testBit(mmu.read(registers.read(Register.HL)), 0); break;
            case 0x47: testBit(Register.A, 0); break;
            case 0x48: testBit(Register.B, 1); break;
            case 0x49: testBit(Register.C, 1); break;
            case 0x4A: testBit(Register.D, 1); break;
            case 0x4B: testBit(Register.E, 1); break;
            case 0x4C: testBit(Register.H, 1); break;
            case 0x4D: testBit(Register.L, 1); break;
            case 0x4E: alu.testBit(mmu.read(registers.read(Register.HL)), 1); break;
            case 0x4F: testBit(Register.A, 1); break;
            case 0x50: testBit(Register.B, 2); break;
            case 0x51: testBit(Register.C, 2); break;
            case 0x52: testBit(Register.D, 2); break;
            case 0x53: testBit(Register.E, 2); break;
            case 0x54: testBit(Register.H, 2); break;
            case 0x55: testBit(Register.L, 2); break;
            case 0x56: alu.testBit(mmu.read(registers.read(Register.HL)), 2); break;
            case 0x57: testBit(Register.A, 2); break;
            case 0x58: testBit(Register.B, 3); break;
            case 0x59: testBit(Register.C, 3); break;
            case 0x5A: testBit(Register.D, 3); break;
            case 0x5B: testBit(Register.E, 3); break;
            case 0x5C: testBit(Register.H, 3); break;
            case 0x5D: testBit(Register.L, 3); break;
            case 0x5E: alu.testBit(mmu.read(registers.read(Register.HL)), 3); break;
            case 0x5F: testBit(Register.A, 3); break;
            case 0x60: testBit(Register.B, 4); break;
            case 0x61: testBit(Register.C, 4); break;
            case 0x62: testBit(Register.D, 4); break;
            case 0x63: testBit(Register.E, 4); break;
            case 0x64: testBit(Register.H, 4); break;
            case 0x65: testBit(Register.L, 4); break;
            case 0x66: alu.testBit(mmu.read(registers.read(Register.HL)), 4); break;
            case 0x67: testBit(Register.A, 4); break;
            case 0x68: testBit(Register.B, 5); break;
            case 0x69: testBit(Register.C, 5); break;
            case 0x6A: testBit(Register.D, 5); break;
            case 0x6B: testBit(Register.E, 5); break;
            case 0x6C: testBit(Register.H, 5); break;
            case 0x6D: testBit(Register.L, 5); break;
            case 0x6E: alu.testBit(mmu.read(registers.read(Register.HL)), 5); break;
            case 0x6F: testBit(Register.A, 5); break;
            case 0x70: testBit(Register.B, 6); break;
            case 0x71: testBit(Register.C, 6); break;
            case 0x72: testBit(Register.D, 6); break;
            case 0x73: testBit(Register.E, 6); break;
            case 0x74: testBit(Register.H, 6); break;
            case 0x75: testBit(Register.L, 6); break;
            case 0x76: alu.testBit(mmu.read(registers.read(Register.HL)), 6); break;
            case 0x77: testBit(Register.A, 6); break;
            case 0x78: testBit(Register.B, 7); break;
            case 0x79: testBit(Register.C, 7); break;
            case 0x7A: testBit(Register.D, 7); break;
            case 0x7B: testBit(Register.E, 7); break;
            case 0x7C: testBit(Register.H, 7); break;
            case 0x7D: testBit(Register.L, 7); break;
            case 0x7E: alu.testBit(mmu.read(registers.read(Register.HL)), 7); break;
            case 0x7F: testBit(Register.A, 7); break;
            case 0x80: resetBit(Register.B, 0); break;
            case 0x81: resetBit(Register.C, 0); break;
            case 0x82: resetBit(Register.D, 0); break;
            case 0x83: resetBit(Register.E, 0); break;
            case 0x84: resetBit(Register.H, 0); break;
            case 0x85: resetBit(Register.L, 0); break;
            case 0x86: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 0)); break;
            case 0x87: resetBit(Register.A, 0); break;
            case 0x88: resetBit(Register.B, 1); break;
            case 0x89: resetBit(Register.C, 1); break;
            case 0x8A: resetBit(Register.D, 1); break;
            case 0x8B: resetBit(Register.E, 1); break;
            case 0x8C: resetBit(Register.H, 1); break;
            case 0x8D: resetBit(Register.L, 1); break;
            case 0x8E: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 1)); break;
            case 0x8F: resetBit(Register.A, 1); break;
            case 0x90: resetBit(Register.B, 2); break;
            case 0x91: resetBit(Register.C, 2); break;
            case 0x92: resetBit(Register.D, 2); break;
            case 0x93: resetBit(Register.E, 2); break;
            case 0x94: resetBit(Register.H, 2); break;
            case 0x95: resetBit(Register.L, 2); break;
            case 0x96: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 2)); break;
            case 0x97: resetBit(Register.A, 2); break;
            case 0x98: resetBit(Register.B, 3); break;
            case 0x99: resetBit(Register.C, 3); break;
            case 0x9A: resetBit(Register.D, 3); break;
            case 0x9B: resetBit(Register.E, 3); break;
            case 0x9C: resetBit(Register.H, 3); break;
            case 0x9D: resetBit(Register.L, 3); break;
            case 0x9E: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 3)); break;
            case 0x9F: resetBit(Register.A, 3); break;
            case 0xA0: resetBit(Register.B, 4); break;
            case 0xA1: resetBit(Register.C, 4); break;
            case 0xA2: resetBit(Register.D, 4); break;
            case 0xA3: resetBit(Register.E, 4); break;
            case 0xA4: resetBit(Register.H, 4); break;
            case 0xA5: resetBit(Register.L, 4); break;
            case 0xA6: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 4)); break;
            case 0xA7: resetBit(Register.A, 4); break;
            case 0xA8: resetBit(Register.B, 5); break;
            case 0xA9: resetBit(Register.C, 5); break;
            case 0xAA: resetBit(Register.D, 5); break;
            case 0xAB: resetBit(Register.E, 5); break;
            case 0xAC: resetBit(Register.H, 5); break;
            case 0xAD: resetBit(Register.L, 5); break;
            case 0xAE: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 5)); break;
            case 0xAF: resetBit(Register.A, 5); break;
            case 0xB0: resetBit(Register.B, 6); break;
            case 0xB1: resetBit(Register.C, 6); break;
            case 0xB2: resetBit(Register.D, 6); break;
            case 0xB3: resetBit(Register.E, 6); break;
            case 0xB4: resetBit(Register.H, 6); break;
            case 0xB5: resetBit(Register.L, 6); break;
            case 0xB6: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 6)); break;
            case 0xB7: resetBit(Register.A, 6); break;
            case 0xB8: resetBit(Register.B, 7); break;
            case 0xB9: resetBit(Register.C, 7); break;
            case 0xBA: resetBit(Register.D, 7); break;
            case 0xBB: resetBit(Register.E, 7); break;
            case 0xBC: resetBit(Register.H, 7); break;
            case 0xBD: resetBit(Register.L, 7); break;
            case 0xBE: mmu.write(registers.read(Register.HL), alu.resetBit(mmu.read(registers.read(Register.HL)), 7)); break;
            case 0xBF: resetBit(Register.A, 7); break;
            case 0xC0: setBit(Register.B, 0); break;
            case 0xC1: setBit(Register.C, 0); break;
            case 0xC2: setBit(Register.D, 0); break;
            case 0xC3: setBit(Register.E, 0); break;
            case 0xC4: setBit(Register.H, 0); break;
            case 0xC5: setBit(Register.L, 0); break;
            case 0xC6: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 0)); break;
            case 0xC7: setBit(Register.A, 0); break;
            case 0xC8: setBit(Register.B, 1); break;
            case 0xC9: setBit(Register.C, 1); break;
            case 0xCA: setBit(Register.D, 1); break;
            case 0xCB: setBit(Register.E, 1); break;
            case 0xCC: setBit(Register.H, 1); break;
            case 0xCD: setBit(Register.L, 1); break;
            case 0xCE: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 1)); break;
            case 0xCF: setBit(Register.A, 1); break;
            case 0xD0: setBit(Register.B, 2); break;
            case 0xD1: setBit(Register.C, 2); break;
            case 0xD2: setBit(Register.D, 2); break;
            case 0xD3: setBit(Register.E, 2); break;
            case 0xD4: setBit(Register.H, 2); break;
            case 0xD5: setBit(Register.L, 2); break;
            case 0xD6: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 2)); break;
            case 0xD7: setBit(Register.A, 2); break;
            case 0xD8: setBit(Register.B, 3); break;
            case 0xD9: setBit(Register.C, 3); break;
            case 0xDA: setBit(Register.D, 3); break;
            case 0xDB: setBit(Register.E, 3); break;
            case 0xDC: setBit(Register.H, 3); break;
            case 0xDD: setBit(Register.L, 3); break;
            case 0xDE: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 3)); break;
            case 0xDF: setBit(Register.A, 3); break;
            case 0xE0: setBit(Register.B, 4); break;
            case 0xE1: setBit(Register.C, 4); break;
            case 0xE2: setBit(Register.D, 4); break;
            case 0xE3: setBit(Register.E, 4); break;
            case 0xE4: setBit(Register.H, 4); break;
            case 0xE5: setBit(Register.L, 4); break;
            case 0xE6: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 4)); break;
            case 0xE7: setBit(Register.A, 4); break;
            case 0xE8: setBit(Register.B, 5); break;
            case 0xE9: setBit(Register.C, 5); break;
            case 0xEA: setBit(Register.D, 5); break;
            case 0xEB: setBit(Register.E, 5); break;
            case 0xEC: setBit(Register.H, 5); break;
            case 0xED: setBit(Register.L, 5); break;
            case 0xEE: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 5)); break;
            case 0xEF: setBit(Register.A, 5); break;
            case 0xF0: setBit(Register.B, 6); break;
            case 0xF1: setBit(Register.C, 6); break;
            case 0xF2: setBit(Register.D, 6); break;
            case 0xF3: setBit(Register.E, 6); break;
            case 0xF4: setBit(Register.H, 6); break;
            case 0xF5: setBit(Register.L, 6); break;
            case 0xF6: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 6)); break;
            case 0xF7: setBit(Register.A, 6); break;
            case 0xF8: setBit(Register.B, 7); break;
            case 0xF9: setBit(Register.C, 7); break;
            case 0xFA: setBit(Register.D, 7); break;
            case 0xFB: setBit(Register.E, 7); break;
            case 0xFC: setBit(Register.H, 7); break;
            case 0xFD: setBit(Register.L, 7); break;
            case 0xFE: mmu.write(registers.read(Register.HL), alu.setBit(mmu.read(registers.read(Register.HL)), 7)); break;
            case 0xFF: setBit(Register.A, 7); break;

            default: throw new UnknownInstructionException("Invalid extended instruction 0x" + Integer.toHexString(instruction));
        }
    }

    /**
     * Returns the next immediate byte
     * */
    private int getImmediateByte() {
        int pc = registers.read(Register.PC);
        int instruction = mmu.read(pc);
        registers.incrementPC();

        return instruction;
    }

    /**
     * Returns the immediate word
     * */
    private int getImmediateWord() {
        int lowerByte = getImmediateByte();
        int higherByte = getImmediateByte();
        return BitUtils.joinBytes(higherByte, lowerByte);
    }

    /**
     * Increments register
     * */
    private void incWordRegister(Register register) {
        registers.write(register, alu.incWord(registers.read(register)));
    }

    private void incByteRegister(Register register) {
        registers.write(register, alu.incByte(registers.read(register)));
    }

    private void decByteRegister(Register register) {
        registers.write(register, alu.decByte(registers.read(register)));
    }

    private void rotateByteRegisterLeft(Register register, boolean throughCarry) {
        registers.write(register, alu.rotateByteLeft(registers.read(register), throughCarry));
    }

    private void loadWordIntoMemory(int address, int word) {
        mmu.write(address, BitUtils.getLowByte(word));
        mmu.write(++address, BitUtils.getHighByte(word));
    }

    private void addWords(Register r1, Register r2) {
        registers.write(r1, alu.addWords(registers.read(r1), registers.read(r2)));
    }

    private void decWordRegister(Register register) {
        registers.write(register, alu.decWord(registers.read(register)));
    }

    private void rotateByteRegisterRight(Register register, boolean throughCarry) {
        registers.write(register, alu.rotateByteRight(registers.read(register), throughCarry));
    }

    private void loadRegisterWithImmediateWord(Register register) {
        registers.write(register, getImmediateWord());
    }

    private void loadRegisterWithImmediateByte(Register register) {
        registers.write(register, getImmediateByte());
    }

    /**
     * Executes jr instruction,
     * only change PC is doJump is true
     * */
    private void jr(boolean doJump) {
        int signedByte = getImmediateByte();
        if (doJump) registers.addSignedByteToPC(signedByte);
    }

    private void SCF() {
        registers.setFlag(Flag.CARRY, true);
        registers.setFlag(Flag.HALF_CARRY, false);
        registers.setFlag(Flag.SUBTRACTION, false);
    }

    private void CCF() {
        registers.setFlag(Flag.CARRY, !registers.getFlag(Flag.CARRY)); // complement
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
    }

    /**
     * Writes r2's val to r1
     * */
    private void loadRegisterFromRegister(Register r1, Register r2) {
        registers.write(r1, registers.read(r2));
    }

    /**
     * (HL) = register
     * */
    private void loadHLMemoryFromRegister(Register register) {
        mmu.write(registers.read(Register.HL), registers.read(register));
    }

    /**
     * register = (HL)
     * */
    private void loadRegisterFromHLMemory(Register register) {
        registers.write(register, mmu.read(registers.read(Register.HL)));
    }

    /**
     * A = A + bytee
     * */
    private void addA(int bytee, boolean addCarry) {
        registers.write(Register.A, alu.addBytes(registers.read(Register.A), bytee, addCarry));
    }

    /**
     * A = A - bytee
     * */
    private void subA(int bytee, boolean subCarry) {
        registers.write(Register.A, alu.subBytes(registers.read(Register.A), bytee, subCarry));
    }

    /**
     * A = A & bytee
     * */
    private void andA(int bytee) {
        registers.write(Register.A, alu.andBytes(registers.read(Register.A), bytee));
    }

    /**
     * A = A XOR bytee
     * */
    private void xorA(int bytee) {
        registers.write(Register.A, alu.xorBytes(registers.read(Register.A), bytee));
    }

    /**
     * A = A OR bytee
     * */
    private void orA(int bytee) {
        registers.write(Register.A, alu.orBytes(registers.read(Register.A), bytee));
    }

    /**
     * Compares bytee with register A
     * */
    private void cpA(int bytee) {
        alu.subBytes(registers.read(Register.A), bytee, false);
    }

    private void ret() {
        registers.write(Register.PC, popWordFromStack());
    }

    private int popWordFromStack() {
        int sp = registers.read(Register.SP);
        int lowByte = mmu.read(sp++);
        int highByte = mmu.read(sp++);
        registers.write(Register.SP, sp);

        return BitUtils.joinBytes(highByte, lowByte);
    }

    private void pushWordToStack(int word) {
        int sp = registers.read(Register.SP);
        mmu.write(--sp, BitUtils.getHighByte(word));
        mmu.write(--sp, BitUtils.getLowByte(word));
        registers.write(Register.SP, sp);
    }

    private void jp() {
        registers.write(Register.PC, getImmediateWord());
    }

    private void call(boolean doCall) {
        int routineAddress = getImmediateWord();
        if (doCall) {
            pushWordToStack(registers.read(Register.PC));
        }
        registers.write(Register.PC, routineAddress);
    }

    private void rst(int restartAddress) {
        pushWordToStack(registers.read(Register.PC));
        registers.write(Register.PC, restartAddress);
    }

    private void shiftByteRegisterLeft(Register register) {
        registers.write(register, alu.shiftByteLeft(registers.read(register)));
    }

    private void shiftByteRegisterRight(Register register, boolean resetBit7) {
        registers.write(register, alu.shiftByteRight(registers.read(register), resetBit7));
    }

    private void swapNibbles(Register byteRegister) {
        registers.write(byteRegister, alu.swapNibbles(registers.read(byteRegister)));
    }

    private void testBit(Register register, int bitNum) {
        alu.testBit(registers.read(register), bitNum);
    }

    private void resetBit(Register register, int bitNum) {
        registers.write(register, alu.resetBit(registers.read(register), bitNum));
    }

    private void setBit(Register register, int bitNum) {
        registers.write(register, alu.setBit(registers.read(register), bitNum));
    }
}
