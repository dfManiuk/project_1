package man.by.frames;

public enum Opcode {

    CONTINUATION_DATA_FRAME(0),
    TEXT_DATA_FRAME(1),
    BINARY_DATA_FRAME(2),
    RESERVED1_DATA_FRAME(3),
    RESERVED2_DATA_FRAME(4),
    RESERVED3_DATA_FRAME(5),
    RESERVED5_DATA_FRAME(6),
    RESERVED6_DATA_FRAME(7),

    CONNECTION_CLOSE_CONTROL_FRAME(8),

    PING_CONTROL_FRAME(9),
    PONG_CONTROL_FRAME(10),


    RESERVED1_CONTROL_FRAME(11),
    RESERVED2_CONTROL_FRAME(12),
    RESERVED3_CONTROL_FRAME(13),
    RESERVED4_CONTROL_FRAME(14),
    RESERVED5_CONTROL_FRAME(15);

    private final int opcode;


    Opcode(int opcode) {
        this.opcode =  opcode;
    }
}
