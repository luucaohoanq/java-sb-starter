package com.orchid.orchidbe.domain.orchid;

import com.orchid.orchidbe.domain.orchid.OrchidDTO.OrchidReq;
import com.orchid.orchidbe.domain.orchid.OrchidDTO.OrchidRes;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrchidService {

    List<OrchidDTO.OrchidRes> getAll();
    Page<OrchidRes> getAll(Pageable pageable);
    OrchidDTO.OrchidRes getById(Long id);
    OrchidDTO.OrchidRes add(OrchidDTO.OrchidReq orchid);
    void update(Long id, OrchidReq orchid);
    void deleteById(Long id);

}
