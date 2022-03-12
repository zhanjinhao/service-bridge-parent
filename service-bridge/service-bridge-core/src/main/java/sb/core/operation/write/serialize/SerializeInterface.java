package sb.core.operation.write.serialize;

public interface SerializeInterface {

    // 序列化一个指令
    void serializeInstruction(Instruction instruction);

    // 序列化一个事务的所有指令
    void serializeInstructionsOneTransaction(InstructionsOneTransaction instructionsOneTransaction);

    // 删除一条指令
    void deSerializeInstruction(Instruction instruction);

    // 序列化一个事务的所有指令
    void deSerializeInstructionsOneTransaction(InstructionsOneTransaction instructionsOneTransaction);

}
