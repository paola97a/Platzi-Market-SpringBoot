package com.platzi.market.persistence.crud;

import com.platzi.market.persistence.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/*CrudRepository<Producto,Integer>: incluye la tabla y el tipo de dato de clave primaria*/

public interface ProductoCrudRepository extends CrudRepository<Producto,Integer> {

    /*@Query(value = "SELECT * FROM productos WHERE id_categoria = ?",nativeQuery = true)*/
    List<Producto> findByIdCategoriaOrderByNombreDesc(int idCategoria);

    Optional<List<Producto>> findByCantidadStockLessThanAndEstado(int cantidadStock, boolean estado);


}
