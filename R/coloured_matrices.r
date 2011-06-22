## Author:  Alex McNamara
## License: Public Domain

library(multicore)

##Verify matrix
##Returns true iff the input matrix has no closing polys on any colour
is_valid_matrix = function(matrix) {
  d <- sqrt(length(matrix))
  
  ##Test for validity on point variant1 (0,d)
  variant1_colour <- matrix[((length(matrix) - d) + 1)]
  
  for (row in 0:(d - 2)) {
    ##Match on left column
    if (matrix[((row * d) + 1)] == variant1_colour) {
      for (column in 2:d) {
        ##Match on inner elements and bottom row
        if (matrix[((row * d) + column)] == variant1_colour && matrix[((d * (d - 1)) + column)] == variant1_colour) {
          return(FALSE)
        }
      }
    }	
  }
  
  ##Test for validity on point variant2 (d,0)
  variant2_colour <- matrix[d]
  for (row in 1:(d - 1)) {
    ##Match on right column
    if (matrix[((row * d) + d)] == variant2_colour) {
      for (column in 1:(d - 1)) {
        ##Match on inner elements and top row
        if (matrix[((row * d) + column)] == variant2_colour && matrix[column] == variant2_colour) {
          return(FALSE)
        }
      }
    }
  }
  
  ##Clean garbage
  remove(variant1_colour, variant2_colour)
  
  ##Matrix is verified
  return(TRUE)
}

##Given a valid d-1 matrix base, will enumerate over all overlap matrices and generate all possible variant matrices; returns a list of valid variants.
generate_variants = function (valid_dprev_matrices, base_dprev_matrix_index) {
  write(paste(base_dprev_matrix_index, "/", length(valid_dprev_matrices)), "")
  valid_variants <- list()
  d <- sqrt(length(valid_dprev_matrices[[1]])) + 1

  for (compare_dprev_matrix_index in base_dprev_matrix_index:length(valid_dprev_matrices)) {
    base_dprev_matrix <- valid_dprev_matrices[[base_dprev_matrix_index]]
    compare_dprev_matrix <- valid_dprev_matrices[[compare_dprev_matrix_index]]
    
    ##Find overlapping elements of each dprev matrix and compare
    base_overlap <- vector()
    for (row in 1:(d - 2)) {
      for(column in 2:(d - 1)) {
        base_overlap[length(base_overlap) + 1] <- base_dprev_matrix[((row * (d - 1)) + column)]
      }
    }
    compare_overlap <- vector()
    for (row in 0:(d - 3)) {
      for(column in 1:(d - 2)) {
        compare_overlap[length(compare_overlap) + 1] <- compare_dprev_matrix[((row * (d - 1)) + column)]
      }
    }
    
    ##If the overlapping submatrices don't match, skip to the next one
    if (!(identical(base_overlap, compare_overlap))) {
      next
    }
    
    ##Build the frame of the potential d matrix (missing 2 corner values)
    potential_d_matrix_frame <- vector()
    potential_d_matrix_frame[(d * d)] <- NA
    
    for (row in 0:(d - 2)) { #Transcribe all elements from base_dprev
      for (column in 1:(d - 1)) {
        potential_d_matrix_frame[((row * d) + column)] <- base_dprev_matrix[((row * (d - 1)) + column)]
      }
    }
    
    for (row in 0:(d - 2)) { #Transcribe far right column (except (d,0)) from compare_dprev
      column <- d
      potential_d_matrix_frame[(((row + 1) * d) + column)] <- compare_dprev_matrix[((row * (d - 1)) + column)] 
    }
    
    for (column in 1:(d - 1)) { #Transcribe the bottom row (except (0,d)) from compare_dprev
      row <- (d - 1)
      potential_d_matrix_frame[((row * d) + (column + 1))] <- compare_dprev_matrix[(((row - 1) * (d - 1)) + column)]
    }
    
    ##Build all possible d matrix variants and verify
    for (variant1 in 0:3) {
      for (variant2 in 0:3) {
        potential_d_matrix_variant <- potential_d_matrix_frame
        potential_d_matrix_variant[((d * (d - 1)) + 1)] <- variant1
        potential_d_matrix_variant[d] <- variant2
        
        if (is_valid_matrix(potential_d_matrix_variant)) {
          valid_variants[[length(valid_variants) + 1]] <- potential_d_matrix_variant
        }
        
        ##Collect garbage
        remove(potential_d_matrix_variant)
      }
    }  
    ##Collect garbage
    remove(row, column, base_overlap, compare_overlap, potential_d_matrix_frame)
  }
  
  return(valid_variants)
}

##Finds all valid matrices of dimension d, based on the valid matrices of d-1
##Input: a list of valid matrices for d-1, matrices are represented as vectors of (d-1)^2 elements
get_valid_d_matrices = function(valid_dprev_matrices) {
  if ((length(valid_dprev_matrices) == 0) || !(typeof(valid_dprev_matrices) == typeof(list()))) {
    print("Matrix list is empty or corrupt")
    return()
  }
  
  ##Iterate through all pairs of dprev matrices
  results <- mclapply(1:length(valid_dprev_matrices), function(idx) {generate_variants(valid_dprev_matrices, idx)})

  ##Parse results (flatten and remove duplicates)
  valid_d_matrices <- list()
  mclapply(1:length(results), function(idx) {
    result <- results[idx]
    if (is.list(result)) {
      for (valid_matrix in result) {
        c(valid_d_matrices, valid_matrix)
      }
    }
  })

  valid_d_matrices <- unique(valid_d_matrices)
  return(valid_d_matrices)
}

##Generates all valid d2 matrix colouring combinations
get_valid_d2_matrices = function() {
  matrix_list <- list()
  for (p00 in 0:3) {
    for (p01 in 0:3) {
      for (p10 in 0:3) {
        for (p11 in 0:3) {
          ##If the matrix doesn't consist of a single colour (these are the only d2s that don't validate) then add it to the list
          if (!(p00 == p01 && p00 == p10 && p00 == p11)) {
            matrix_list[[length(matrix_list) + 1]] <- (c(p00,p01,p10,p11))
          }
        }
      }
    }
  }
  ##Write all of the valid d2 matrices to disk and return
  save(matrix_list, file = "d2")
  
  return(matrix_list)
}

##Finds all valid four colouring matrices up to d18
execute = function() {
  ##Start by building d2 matrix list
  dprev_matrices <- list()
  dprev_matrices <- get_valid_d2_matrices()
  
  for(d in 3:18) {
    write(paste("Building d", d, " matrices (", date(), ")", sep = ""), "")
    start_time <- unclass(Sys.time())
    
    ##Find the next level of valid matrices
    d_matrices <- list()
    d_matrices <- get_valid_d_matrices(dprev_matrices)
    
    ##Print summary info, and record valid solutions on filesystem
    write(paste("Completed building d", d, " group", sep = ""), "")
    write(paste("\tTime:", (unclass(Sys.time()) - start_time), "seconds"), "")
    write(paste("\tValid solutions:", length(d_matrices)), "")
    save(d_matrices, file = paste("d", d, sep = ""))
    write("Result set saved to filesystem.", "")
    ##Setup the next iteration and collect garbage
    dprev_matrices <- d_matrices
    remove(d_matrices, start_time)
  }
}

options(warn = 1)
execute()
