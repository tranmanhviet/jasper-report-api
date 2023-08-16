package vn.com.itechcorp.jasper.base.service;

import vn.com.itechcorp.base.exception.APIException;
import vn.com.itechcorp.base.persistence.model.interfaces.BaseEntity;
import vn.com.itechcorp.base.service.dto.BaseDtoCreate;
import vn.com.itechcorp.base.service.dto.DtoGet;
import vn.com.itechcorp.base.service.dto.DtoUpdate;
import vn.com.itechcorp.base.service.filter.PageOfData;
import vn.com.itechcorp.base.service.filter.PaginationInfo;
import vn.com.itechcorp.jasper.base.filter.BaseFilter;

import java.io.Serializable;
import java.util.List;

public interface BaseService<OUT extends DtoGet<T, ID>, T extends BaseEntity<ID>, ID extends Serializable> {

    /**
     * Convert database entry to a response object
     *
     * @return the response object
     */
    OUT convert(T t);

    /**
     * Obtains an object matching a given identifier
     *
     * @param id the identifier
     * @return the matching object
     */
    OUT getById(ID id) throws APIException;

    /**
     * Return a list of objects
     *
     * @return a list of objects of the given class
     */
    List<OUT> getAll() throws APIException;

    /**
     * Return a list of objects
     *
     * @param filter
     * @return a list of objects of the given class
     */
    List<OUT> getAll(BaseFilter filter) throws APIException;

    /**
     * Return a lists of objects, with paging
     *
     * @param pagingInfo
     * @return list of objects
     */
    PageOfData<OUT> getPageOfData(PaginationInfo pagingInfo) throws APIException;

    /**
     * Return a lists of objects, with filter
     *
     * @param filter
     * @param pagingInfo
     * @return list of objects
     */
    PageOfData<OUT> getPageOfData(BaseFilter filter, PaginationInfo pagingInfo) throws APIException;

    /**
     * Create a new object
     *
     * @param entity   the object to be created
     * @return the created object
     */
    OUT create(BaseDtoCreate<T, ID> entity) throws APIException;

    /**
     * Modify an object
     *
     * @param entity   the object to be modified
     * @return the updated object
     */
    OUT update(DtoUpdate<T, ID> entity) throws APIException;

    /**
     * Completely deletes an object from the database
     *
     * @param id the object identifier to delete
     * @return the deleted object
     */
    void deleteByID(ID id) throws APIException;

}
