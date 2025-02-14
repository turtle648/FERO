export const assets = ['hair', 'face', 'body'].reduce((acc, category) => {
  const count = {
    hair: 10,
    face: 15,
    body: 8,
  }[category];

  acc[category] = Array.from({ length: count }, (_, i) => [
    require(`@/assets/character/${category}/${category}${i}.png`),
    i,
  ]);

  return acc;
}, {});
