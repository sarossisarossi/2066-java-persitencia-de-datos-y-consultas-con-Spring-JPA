import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class VerificarConexion implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("SELECT 1");  // Ejecuta una consulta simple
            System.out.println("✅ Conexión exitosa a la base de datos PostgreSQL");
        } catch (Exception e) {
            System.out.println("❌ No se pudo conectar a la base de datos:");
            e.printStackTrace();
        }
    }
}
