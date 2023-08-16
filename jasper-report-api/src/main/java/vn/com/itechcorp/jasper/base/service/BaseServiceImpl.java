package vn.com.itechcorp.jasper.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.itechcorp.base.exception.APIException;
import vn.com.itechcorp.base.exception.BlankIdentifierException;
import vn.com.itechcorp.base.exception.ObjectNotFoundException;
import vn.com.itechcorp.base.persistence.model.interfaces.BaseEntity;
import vn.com.itechcorp.base.persistence.repository.OffsetBasedPageable;
import vn.com.itechcorp.base.service.dto.BaseDtoCreate;
import vn.com.itechcorp.base.service.dto.DtoGet;
import vn.com.itechcorp.base.service.dto.DtoUpdate;
import vn.com.itechcorp.base.service.filter.PageOfData;
import vn.com.itechcorp.base.service.filter.PaginationInfo;
import vn.com.itechcorp.jasper.base.filter.BaseFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Transactional
public abstract class BaseServiceImpl<OUT extends DtoGet<T, ID>, T extends BaseEntity<ID>, ID extends Serializable> implements BaseService<OUT, T, ID> {

    public abstract MongoRepository<T, ID> getRepository();

    public abstract MongoTemplate getMongoTemplate();

    public abstract Class<T> clazz();

    protected Set<String> getSortableColumns() {
        return null;
    }

    @Override
    public OUT getById(ID id) throws APIException {
        T object = getRepository().findById(id).orElse(null);
        if (object != null) return convert(object);
        else throw new ObjectNotFoundException("Not found (id-{0})", (Throwable) id);
    }

    @Override
    public List<OUT> getAll() throws APIException {
        List<T> results = getRepository().findAll();
        if (results == null || results.isEmpty()) return null;
        return results.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OUT> getAll(BaseFilter filter) throws APIException {
        List<T> results = getMongoTemplate().find(filter.toQuery(), clazz());
        if (results == null || results.isEmpty()) return null;
        return results.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public PageOfData<OUT> getPageOfData(PaginationInfo pagingInfo) throws APIException {
        OffsetBasedPageable pageable = pagingInfo.toPageable(getSortableColumns());
        Page<T> result = getRepository().findAll(pageable);
        if (result.isEmpty()) return new PageOfData<>();
        return new PageOfData<>(result.toList().stream().map(this::convert).collect(Collectors.toList()),
                pageable.getOffset(), pageable.getPageSize(), result.getTotalElements());
    }

    @Override
    public PageOfData<OUT> getPageOfData(BaseFilter filter, PaginationInfo pagingInfo) throws APIException {
        OffsetBasedPageable pageable = pagingInfo.toPageable(getSortableColumns());
        PageRequest pr = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        List<T> result = getMongoTemplate().find(filter.toQuery().with(pr), clazz());
        if (result.isEmpty()) return new PageOfData<>();

        long total = getMongoTemplate().count(filter.toQuery(), clazz());
        return new PageOfData<>(result.stream().map(this::convert).collect(Collectors.toList()),
                pageable.getOffset(), pageable.getPageSize(), total);
    }

    @Override
    public OUT create(BaseDtoCreate<T, ID> entity) throws APIException {
        return convert(getRepository().save(entity.toEntry()));
    }

    @Override
    public OUT update(DtoUpdate<T, ID> entity) throws APIException {
        if (entity.getId() == null) throw new BlankIdentifierException("Missing (id)");

        T object = getRepository().findById(entity.getId()).orElse(null);
        if (object == null) throw new ObjectNotFoundException("Not found (id-{0})", (Throwable) entity.getId());

        if (!entity.apply(object)) return convert(object);
        return convert(getRepository().save(object));
    }

    @Override
    public void deleteByID(ID id) throws APIException {
        if (id == null) throw new BlankIdentifierException("Missing (id)");
        getRepository().deleteById(id);
    }
}