package pl.putnet.fitmanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import pl.putnet.fitmanager.model.Clazz;

public interface ClazzRepository extends CrudRepository<Clazz, Integer> {
	Optional<Clazz> findById(Integer id);

	List<Clazz> findByDate(LocalDate date);
}
