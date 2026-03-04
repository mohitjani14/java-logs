package com.logdownloader.repository;

import com.logdownloader.model.DownloadJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadJobRepository extends JpaRepository<DownloadJob, Long> {

}