package par;

/**
 * Сервис для получения номиналов банкнот.
 * В реальной жизни номиналы банкнот хранятся в базе данных или конфигурационном файле.
 */
public interface ParService {

    Par getPar(Integer value);
}
