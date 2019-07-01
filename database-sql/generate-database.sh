#!/bin/bash -xe

#
# Main procedures:
#
SCRIPTPATH=$0
CURRDIR=$(dirname "${SCRIPTPATH}")


echo "Generating database."
echo
echo -n "Constructing schema: "
for fname in $(ls Schema); do
  echo "$(cat ${CURRDIR}/Schema/${fname})
  quit" | sqlplus -s demooracle/demooracle
  echo -n "."
done
echo
echo
echo -n "Running seeds: "
for fname in $(ls Seeds); do
  echo "$(cat ${CURRDIR}/Seeds/${fname})
  quit" | sqlplus -s demooracle/demooracle 
  echo -n "."
done
echo
echo

