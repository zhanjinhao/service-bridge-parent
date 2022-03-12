package sb.core.operation.write;

/**
 * 真正的写操作已经在 AbstractBufferedBasicWriteOperation 类中执行完毕，事务的作用是控制指令写入外存，如文件或消息队列时的原子性。
 * 写入外存的操作由接口 SerializeInterface 完成
 */
public interface TransactionInterface {

    // 每一个事务是一个原子性操作，begin()的作用是开启一个事务，映射到AbstractTransactionBasicWriteOperation类中就是加一行分割符
    void begin();

    // 提交的作用是将指令写入外存，这一步进行完成之后，无法再对之前的写操作进行撤销
    void commit();

    // 撤销当前事务中已经执行过的的写操作，包括指令对应的逆过程和擦除外存中的记录
    void rollBack();

}